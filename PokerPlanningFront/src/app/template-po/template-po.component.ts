import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-template-po',
  templateUrl: './template-po.component.html',
  styleUrls: ['./template-po.component.css']
})
export class TemplatePOComponent implements OnInit {
  developers: any[] = [];
  role: string | null = '';

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit(): void {
    this.role = localStorage.getItem('role');
    if (this.role !== 'PRODUCTOWNER') {
      // Rediriger l'utilisateur s'il n'est pas Product Owner
      this.router.navigate(['/not-authorized']);
      return;
    }

    this.getAllDevelopers();
  }

  getAllDevelopers(): void {
    const token = localStorage.getItem('token');
    if (token) {
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });

      this.http.get<any[]>('http://localhost:8080/api/users/developers', { headers }).subscribe(
        (data: any[]) => this.developers = data,
        error => console.error('Error fetching developers', error)
      );
    } else {
      console.error('No token found');
    }
  }

  enableDeveloper(developerId: string): void {
    const token = localStorage.getItem('token');
    if (token) {
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });

      this.http.put(`http://localhost:8080/api/users/developers/${developerId}/enable`, {}, { headers }).subscribe(
        () => {
          console.log('Developer enabled successfully!');
          this.getAllDevelopers();
        },
        error => console.error('Error enabling developer', error)
      );
    }
  }

  disableDeveloper(developerId: string): void {
    const token = localStorage.getItem('token');
    if (token) {
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });

      this.http.put(`http://localhost:8080/api/users/developers/${developerId}/disable`, {}, { headers }).subscribe(
        () => {
          console.log('Developer disabled successfully!');
          this.getAllDevelopers();
        },
        error => console.error('Error disabling developer', error)
      );
    }
  }

  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/sign-in']);
  }
}
