import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, from } from 'rxjs';
import { AuthService } from '../auth.service';
import { API_CONFIG } from '../../../assets/data/api-config';
import { Firestore, collection, query, getDocs, CollectionReference, DocumentData, addDoc, updateDoc, doc, deleteDoc, setDoc } from '@angular/fire/firestore';

@Injectable({
  providedIn: 'root'
})
export class ClientesService {

  private apiUrl = `${API_CONFIG}/clientes`;
  private token?: string;
  private clientesCollection: CollectionReference<DocumentData>;

  constructor(private http: HttpClient, private _authService: AuthService, private firestore: Firestore) {
    this.clientesCollection = collection(this.firestore, 'clientes');
  }

  getClientes() {
    this.token = this._authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}`);
    return this.http.get<any>(`${this.apiUrl}/obtener`, { headers });
  }

  insertarCliente(cliente: any) {
    const url = this.apiUrl + `/insertar`;
    const token = this._authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<any>(url, cliente, { headers });
  }

  actualizarCliente(cliente: any) {
    const token = this._authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<any>(`${this.apiUrl}/editar`, cliente, { headers });
  }

  eliminarCliente(cliente: any) {
    const token = this._authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<any>(`${this.apiUrl}/eliminar`, cliente, { headers });
  }

  // Métodos Firebase

  getClientesFirebase(): Observable<any[]> {
    const clientesQuery = query(this.clientesCollection);
    return from(getDocs(clientesQuery).then(querySnapshot => {
      return querySnapshot.docs.map(doc => ({
        id: doc.id,
        ...doc.data()
      }));
    }));
  }

  addClienteFirebase(cliente: any): Promise<any> {
    const docRef = doc(this.clientesCollection, cliente.idCliente.toString()); // Especificamos el ID del documento
    return setDoc(docRef, cliente);
    //return addDoc(this.clientesCollection, cliente);
  }

  updateClienteFirebase(id: string, cliente: any): Promise<void> {
    const clienteDoc = doc(this.firestore, `clientes/${id}`);
    return updateDoc(clienteDoc, cliente);
  }

  deleteClienteFirebase(id: string): Promise<void> {
    const clienteDoc = doc(this.firestore, `clientes/${id}`);
    return deleteDoc(clienteDoc);
  }
}
