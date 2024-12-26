import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MenuItem } from 'primeng/api';
import { AuthService } from './auth.service';


@Injectable({
  providedIn: 'root'
})
export class MenuService {

  constructor(private http: HttpClient) { }

  
  getMenu(): Observable<MenuItem[]>{
    return this.http.get<MenuItem[]>('assets/data/menu.json');
  }

}
