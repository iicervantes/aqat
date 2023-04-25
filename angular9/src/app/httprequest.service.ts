import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpErrorResponse } from '@angular/common/http';

import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { IIpStatus } from "./IIpStatus";

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  configUrl = 'http://localhost:8080/cidr/';
  aqatCitiesUrl = "https://api.openaq.org/v2/cities?country=US";
  aqatCityMeasurements = "https://api.openaq.org/v2/latest?limit=100&page=1&offset=0&sort=desc&radius=1000&order_by=lastUpdated&country=US&"
  constructor(private http: HttpClient) { }

  // APIs
  getAllAddresses() {
    return this.http.get<IIpStatus[]>(this.configUrl + "getAll")
      .pipe(
        catchError(this.handleError) // then handle the error
      );
  }

  addIp(ip:string):Observable<any> {
    return this.http.post(this.configUrl + "add",{"ip":ip},{ responseType: 'text' })
      .pipe(
        catchError(this.handleError) // then handle the error
      );
  }

  acquireIp(ip:string):Observable<any> {
    return this.http.post(this.configUrl + "acquire",{"ip":ip},{ responseType: 'text' })
      .pipe(
        catchError(this.handleError) // then handle the error
      );
  }

  releaseIp(ip:string):Observable<any> {
    return this.http.post(this.configUrl + "release",{"ip":ip},{ responseType: 'text' })
      .pipe(
        catchError(this.handleError) // then handle the error
      );
  }

  /***** AQAT APIs *****/

  //Get the first 100 cities from API
  getCities()
  {
    return this.http.get<any>(this.aqatCitiesUrl)
      .pipe(
        catchError(this.handleError) // then handle the error
      );
  }
  getCityMeasurements(city: string):Observable<any>
  {
    return this.http.get<any>(this.aqatCityMeasurements+"city="+city)
      .pipe(
        catchError(this.handleError) // then handle the error
      );
  }
  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Backend returned code ${error.status}, body was: `, error.error);
    }
    // Return an observable with a user-facing error message.
    return throwError(() => new Error('Something bad happened; please try again later.'));
  }

}
