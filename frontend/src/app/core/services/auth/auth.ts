import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Jwt } from '@/core/services/jwt/jwt';
import { User as UserModel } from '@/core/user/models/user.model';
import { User } from '@/core/user/services/user.service';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from '@env/environment.development';

@Injectable({
  providedIn: 'root',
})
export class Auth {

  private http = inject(HttpClient);
  private jwtService = inject(Jwt);
  private userService = inject(User)



  public login(email: string, password: string): Observable<void> {
    return this.http.post<{ accessToken: string; refreshToken: string, expiresIn: number }>(`${environment.apiUrl}/api/auth/login`, { email, password }).pipe(
      tap((data) => {
        this.jwtService.saveToken({
          accessToken: data.accessToken,
          refreshToken: data.refreshToken,
          expiresAt: Date.now() + data.expiresIn * 1000
        });
      }),
      map(() => void 0)
    );
  }

  public getMe(): Observable<UserModel> {
    return this.http.get<UserModel>(`${environment.apiUrl}/api/auth/me`).pipe(
      tap((data) => this.userService.saveUser(data))
    );
  }

  public refreshToken(): Observable<{ accessToken: string; refreshToken: string; expiresIn: number }> {
    const token = this.jwtService.getToken();
    if (!token) {
      return new Observable(observer => { observer.error('No token'); });
    }
    return this.http.post<{ accessToken: string; refreshToken: string; expiresIn: number }>(`${environment.apiUrl}/api/auth/refresh`, { refreshToken: token.refreshToken }).pipe(
      tap((data) => {
        this.jwtService.saveToken({
          accessToken: data.accessToken,
          refreshToken: data.refreshToken,
          expiresAt: Date.now() + data.expiresIn * 1000
        });
      })
    );
  }

  public logout(): void {
    this.jwtService.removeToken();
    this.userService.removeUser();
  }






}

