import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { AuthService } from './auth.service';
import { API_CONFIG } from '../../assets/data/api-config';

@Injectable({
  providedIn: 'root'
})
export class ReservasService {

  private apiUrl = `${API_CONFIG}/reservas`
  private token?: string
    

  constructor(private http: HttpClient,private _authService: AuthService) { }


  
  getReservas() {
    this.token = this._authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}`);
    return this.http.get<any>(`${this.apiUrl}/obtener`, { headers })
  }

  insertarReserva(reserva: any){
    const url = this.apiUrl+`/insertar`;
    const token = this._authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<any>(url, reserva ,{ headers });
  }
  
  actualizarReserva(reserva: any){
    const token = this._authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<any>(`${this.apiUrl}/editar`, reserva ,{ headers });
  }

  eliminarReserva(reserva: any){
    const token = this._authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<any>(`${this.apiUrl}/eliminar`, reserva, { headers });
  }
  
}
