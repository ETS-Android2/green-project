import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Environment } from '../interfaces/environment';

@Injectable({
  providedIn: 'root'
})

export class ApiService {
  // attributes
  baseURL = "http://201.171.7.12:7000/api?action=";

  // constructor
  constructor(private http: HttpClient) { 
  }

  getFruitsReadings(): Observable<any>{
    return this.http.get(this.baseURL + 'get_enviromentVariables');
  }
}

