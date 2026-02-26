import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {finalize, Observable, tap} from 'rxjs';
import jwt_decode from "jwt-decode";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http: HttpClient, private router: Router) {
  }

  login(username: string, password: string): Observable<any> {
    const credentials = { username, password };
    return this.http.post<any>('/api/login', credentials, { withCredentials: true }).pipe(
      tap((response) => {
        // Handle successful login
        this.addTokenInfoToLocalStorage(response.token); // Store the token
        this.router.navigate(['']); // Navigate to the home page or another route
      })
    );
  }

  register(username: string, email: string, password: string): Observable<any> {
    const credentials = { username, email, password };

    return this.http.post<any>('/api/register', credentials, { withCredentials: true }).pipe(
      tap((response) => {
        this.addTokenInfoToLocalStorage(response.token);
      })
    );
  }


  logout(): Observable<void> {
    return this.http.post<void>('/api/logout', null, { withCredentials: true }).pipe(
      finalize(() => {
        this.removeTokenInfoFromLocalStorage();
        this.router.navigateByUrl('/');
      })
    );
  }

  addTokenInfoToLocalStorage(token: string): void {
    try {
      const decodedToken:any = jwt_decode(token);
      console.log(decodedToken)
      const role = decodedToken.role.authority;
      const username = decodedToken.sub;
      localStorage.setItem('userRole', role);
      localStorage.setItem('username', username);
    } catch (error) {
      console.log("Something went wrong!", error);
    }
  }

  removeTokenInfoFromLocalStorage(){
    localStorage.removeItem('userRole');
    localStorage.removeItem('username');
  }

  isAdmin() {
    return localStorage.getItem('userRole') === "ROLE_ADMIN";
  }

  isLoggedIn(){
    return !!localStorage.getItem('userRole');
  }

  getUsername(){
    return localStorage.getItem('username');
  }

  getUserRole() {
    return localStorage.getItem('userRole')?.replace('ROLE_', '');
  }

}
