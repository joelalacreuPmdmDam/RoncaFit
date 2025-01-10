import { ClientesService } from '../../services/usuarios/clientes.service';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ToolbarModule } from 'primeng/toolbar';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { NgIf } from '@angular/common';
import { ConfirmationService, MessageService } from 'primeng/api';
import { TooltipModule } from 'primeng/tooltip';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { FloatLabelModule } from 'primeng/floatlabel';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [TableModule,TooltipModule,ButtonModule,InputTextModule,ToolbarModule,ConfirmDialogModule,DialogModule,NgIf,FloatLabelModule,ReactiveFormsModule,FormsModule,ToastModule],
  templateUrl: './clientes.component.html',
  styleUrl: './clientes.component.scss',
  providers: [MessageService,ConfirmationService]
})
export class ClientesComponent {

  //INICIO VARIABLES COMPONENTES HTML
  @ViewChild('dt1') dt1: Table | undefined; //TABLA
  @ViewChild('inputSearch') inputSearch!: ElementRef; //FILTRO GLOBAL TABLA
  selectedSize = { name: 'Small', class: 'p-datatable-sm' }; //HACER FILAS MÁS PEQUEÑAS
  //END
  //INICIO GESTIÓN PERMISOS
  completedRequests!: number;
  totalRequests!: number;
  loadingInfo: boolean = true;
  //END GESTIÓN PERMISOS
  listaClientes: any = [];
  listaClientesFirebase: any;
  //INICIO ATRIBUTOS FILTROS
  valorFiltroGlobal: string = '';
  //FIN ATRIBUTOS FILTROS
  //INICIO VARIABLES CREAR USUARIO
  showCrearUsuarioDialog: boolean = false;
  formGroup: FormGroup;
  //FIN VARIABLES CREAR USUARIO
  //INICIO VARIABLES EDITAR USUARIO
  showEditarUsuarioDialog: boolean = false;
  usuarioEditado: any = {} //Copia del usuario seleccionado para editar
  //FIN VARIABLES EDITAR USUARIO

  constructor(private _clientesService: ClientesService,private fb: FormBuilder,private messageService: MessageService,private confirmationService: ConfirmationService){
    this.formGroup = this.fb.group({
      fbDni: ['',Validators.required],
      fbNombre: ['',Validators.required],
      fbApellidos: ['',Validators.required],
      fbMail: ['',Validators.required],
      fbUsername: ['',Validators.required],
      fbContrasenya: ['',Validators.required]
    });
  }

  ngOnInit(): void {
    this.totalRequests = 1;
    this.completedRequests = 0;
    this.getClientes();
    this.getClientesFirebase();
  }

  //INICIO GESTIÓN PERMISOS
  checkRequestsCompleted() {
    this.completedRequests++;
    if (this.completedRequests === this.totalRequests) {
      this.loadingInfo = false;
    }
  }

  async getClientes() {
    this.loadingInfo = true;
    try {
      const data = await this._clientesService.getClientes().toPromise();
      this.listaClientes = data.clientes;
      this.checkRequestsCompleted();
    } catch (error) {
      console.error('Error al obtener los clientes:', error);
    }
  }
  
  getClientesFirebase(){
    this.loadingInfo = true;
    this._clientesService.getClientesFirebase().subscribe(
      (data) => {
        this.listaClientesFirebase = data;
        this.checkRequestsCompleted();
      },
      (error) => {
        console.error('Error al obtener los clientes desde el Firebase:', error);
      }
    );
  }



  //INICIO MÉTODOS EDITAR USUARIO
  openEditDialog(usuarioSeleccionado: any){
    this.showEditarUsuarioDialog = true;
    this.usuarioEditado = { ...usuarioSeleccionado };
  }

  actualizarCliente(){
    this._clientesService.actualizarCliente(this.usuarioEditado).subscribe(
      (response) => {
        this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Cliente actualizado correctamente', life: 3000 });
        this.totalRequests = 1;
        this.completedRequests = 0;
        this.getClientes();
        this.actualizarClienteFirebase(this.usuarioEditado);
        this.closeEditDialog();
      },
      (error) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudo actualizar el cliente', life: 3000 });
      }
    );
  }

  actualizarClienteFirebase(usuarioEditado: any){
    var idDocumento = this.buscarIdFirebasePorIdCliente(usuarioEditado.idCliente);
    if (idDocumento !== undefined){
      this._clientesService.updateClienteFirebase(idDocumento, {idCliente: usuarioEditado.idCliente, nombreCliente: usuarioEditado.nombreUsuario}).then(() => {
        this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Cliente actualizado en firebase correctamente', life: 3000 });
        this.showEditarUsuarioDialog = false;
      }).catch((error) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudo actualizar el cliente en firebase', life: 3000 });
      });
    }else{
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'El ID del documento firebase es \'undefined\'', life: 3000 });
    }    
  }


  closeEditDialog(){
    this.showEditarUsuarioDialog = false;
    this.usuarioEditado = {};
  }
  //FIN MÉTODOS EDITAR USUARIO

  //INICIO MÉTODOS CREAR USUARIO
  openCrearClienteDialog(){
    this.showCrearUsuarioDialog = true;
  }

  crearCliente(){
    var newCliente: any = {
      dni: this.formGroup.value.fbDni,
      nombre: this.formGroup.value.fbNombre,
      apellidos: this.formGroup.value.fbApellidos,
      mail: this.formGroup.value.fbMail,
      nombreUsuario: this.formGroup.value.fbUsername,
      contrasenya: this.formGroup.value.fbContrasenya
    };
    this.insertarCliente(newCliente);
  }

  async insertarCliente(newCliente: any) {
    var copiaCliente = { ...newCliente };  
    try {
      await this._clientesService.insertarCliente(newCliente).toPromise();
      this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Cliente insertado correctamente', life: 3000 });  
      this.totalRequests = 2;
      this.completedRequests = 0;
      await this.getClientes();
      this.insertarClienteFirebase(copiaCliente);
      this.getClientesFirebase();  
      this.closeCrearDialog();
      this.formGroup.reset();
    } catch (error) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudo insertar el cliente', life: 3000 });
    }
  }
  

  insertarClienteFirebase(newCliente: any){
    var idCliente = this.buscarIdClientePorAtributos(newCliente);
    if (idCliente !== undefined){
      this._clientesService.addClienteFirebase({idCliente: idCliente, nombreCliente: newCliente.nombreUsuario}).then(() => {
        this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Cliente insertado en firebase correctamente', life: 3000 });
        this.showEditarUsuarioDialog = false;
      }).catch((error) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudo insertar el cliente en firebase', life: 3000 });
      });
    }else{
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'El ID del documento firebase es \'undefined\'', life: 3000 });
    }       
  }

  closeCrearDialog(){
    this.showCrearUsuarioDialog = false;
  }
  //FIN MÉTODOS CREAR USUARIO

  //INICIO MÉTODOS ELIMINAR USUARIO
  deleteClienteDialog(cliente: any) {
    this.confirmationService.confirm({
        message: `Vas a eliminar al cliente ${cliente.nombre} ${cliente.apellidos}, con ID -> ${cliente.idCliente}. Quieres continuar?`,
        header: 'Eliminar cliente',
        icon: 'pi pi-exclamation-triangle',
        acceptLabel: 'Sí',
        accept: () => {
            this.eliminarCliente(cliente.idCliente);
        }
    });
  }

  eliminarCliente(idCliente: number) {
    this._clientesService.eliminarCliente({idCliente: idCliente}).subscribe(
        (response) => {
          this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Cliente eliminado correctamente', life: 3000 });
          this.totalRequests = 2;
          this.completedRequests = 0;
          this.getClientes();
          this.eliminarClienteFirebase(idCliente);
          this.getClientesFirebase();
        },
        (error) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudo eliminar al cliente', life: 3000 });
        }
      );
  }

  eliminarClienteFirebase(idCliente: number){
    var idDocumento = this.buscarIdFirebasePorIdCliente(idCliente);
    if (idDocumento !== undefined){
      this._clientesService.deleteClienteFirebase(idDocumento).then(() => {
        this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Cliente eliminado en firebase correctamente', life: 3000 });
      }).catch((error) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudo eliminar el cliente en firebase', life: 3000 });
      });
    }else{
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'El ID del documento firebase es \'undefined\'', life: 3000 });
    }    
  }
  //FIN MÉTODOS ELIMINAR CLIENTE


  //MÉTODO FILTRO GLOBAL
  onInput(event: Event) {
    if (event.target instanceof HTMLInputElement && this.dt1) {
      this.dt1.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }
  }

  //MÉTODO PARA LIMPIAR LOS FILTROS
  clear(table: Table) {
    this.totalRequests = 1;
    this.completedRequests = 0;
    table.clear();
    this.getClientes();
    this.valorFiltroGlobal = '';
  }

  buscarIdFirebasePorIdCliente(idCliente: number): string | undefined {
    const clienteEncontrado = this.listaClientesFirebase.find((cliente: any) => cliente.idCliente === idCliente);  
    if (clienteEncontrado) {
      return clienteEncontrado.id;
    }
    return undefined;
  }

  buscarIdClientePorAtributos(newCliente: any): number | undefined {
    const clienteEncontrado = this.listaClientes.find((cliente: any) => cliente.mail === newCliente.mail && cliente.dni === newCliente.dni 
    && cliente.nombre === newCliente.nombre && cliente.apellidos === newCliente.apellidos && cliente.nombreUsuario === newCliente.nombreUsuario);
    if (clienteEncontrado) {
      return clienteEncontrado.idCliente;
    }
    return undefined;
  }
  
  
}