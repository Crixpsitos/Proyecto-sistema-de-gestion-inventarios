import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class Jwt {

  private localStorage = window.localStorage;

  public saveToken(token: { accessToken: string; refreshToken: string, expiresAt: number }): void {
    this.localStorage.setItem('token', JSON.stringify(token));
  }



  public getToken(): { accessToken: string; refreshToken: string, expiresAt: number } | null {
    const token = this.localStorage.getItem('token');
    if (token) {
      return JSON.parse(token);
    }
    return null;
  }


  public removeToken(): void {
    this.localStorage.removeItem('token');
  }


}
