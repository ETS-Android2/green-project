import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { ProductionLine } from '../interfaces/production-line';
import { Fruit } from '../interfaces/fruit';
import { EnvironmentReadings } from '../interfaces/environment-readings';
import { FruitReadings } from '../interfaces/fruit-readings';
import { Reading } from '../interfaces/reading';

@Injectable({
  providedIn: 'root'
})

export class ApiService {
  // attributes
  // baseURL = "http://189.223.79.36:7000/";
  baseURL = "http://127.0.0.1:5000/";

  // constructor
  constructor(private http: HttpClient) { 
  }

  // ------------- Methods -------------
  // get data
  getFruits(): Observable<Fruit[]>{
    return this.http.get<Fruit[]>(this.baseURL + 'get-fruits?');
  }

  getProductionLine(): Observable<ProductionLine[]>{
    return this.http.get<ProductionLine[]>(this.baseURL + 'get-productionLines?');
  }

  getFruitReadings(productionLine=""): Observable<FruitReadings[] | Reading>{
    return this.http.get<FruitReadings[] | Reading>(this.baseURL + 'get-readings?'+productionLine);
  }

  getEnvironmentReadings(): Observable<EnvironmentReadings[]>{
    return this.http.get<EnvironmentReadings[]>(this.baseURL + 'get-enviromentVariables?');
  }

  // set data
  insertFruit(fruit: FormData){
    return this.http.post(this.baseURL + 'insertFruit', fruit);
  }

  // insert the requirements for the fruit 

  insertFruitRequirements(readings : Object){
    return this.http.post(this.baseURL+"insertFruitRequirements", readings)
  }
  
  // stablish the realtion between the production line and the fruit to be scanned.
  setFruit_productionLine(fruit_productionLine: FormData){
    return this.http.post(this.baseURL+ "setFruit", fruit_productionLine);
  }

  // update data

}

