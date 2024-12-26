import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { AuthService } from '../auth.service';
import { API_CONFIG } from '../../../assets/data/api-config';

@Injectable({
  providedIn: 'root'
})
export class InstructoresService {

  private apiUrl = `${API_CONFIG}/instructores`
  private token?: string
    

  constructor(private http: HttpClient,private _authService: AuthService) { }


  
  getInstructores() {
    this.token = this._authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}`);
    return this.http.get<any>(`${this.apiUrl}/obtener`, { headers })
  }

  getActividadesInstructor(idInstructor: number) {
    this.token = this._authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}`);
    return this.http.get<any>(`${this.apiUrl}/${idInstructor}/actividades`, { headers })
  }
  
  editarActividadesInstructor(idInstructor: number,actividades: any){
    const token = this._authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<any>(`${this.apiUrl}/${idInstructor}/actividades`, actividades ,{ headers });
  }

  getInstructoresDispo(info: any) {
    this.token = this._authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}`);
    return this.http.post<any>(`${this.apiUrl}/disponibles`, info,{ headers })
  }

}
