import { Injectable } from '@angular/core';
import { DatePipe } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { API_CONFIG } from '../../../assets/data/api-config';

@Injectable({
  providedIn: 'root'
})
export class ClientesService {

  private apiUrl = `${API_CONFIG}/clientes`
  private token?: string
    

  constructor(private http: HttpClient,private _authService: AuthService) { }


  
  getClientes() {
    this.token = this._authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}`);
    return this.http.get<any>(`${this.apiUrl}/obtener`, { headers })
  }
  

  insertarCliente(cliente: any){
    const url = this.apiUrl+`/insertar`;
    const token = this._authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<any>(url, cliente ,{ headers });
  }
  
  actualizarCliente(cliente: any){
    const token = this._authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<any>(`${this.apiUrl}/editar`, cliente ,{ headers });
  }

  eliminarCliente(cliente: any){
    const token = this._authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<any>(`${this.apiUrl}/eliminar`, cliente, { headers });
  }
}
