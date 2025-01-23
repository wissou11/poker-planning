import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard-layout',
  templateUrl: './dashboard-layout.component.html',
  styleUrls: ['./dashboard-layout.component.css']
})
export class DashboardLayoutComponent implements OnInit {
  userRole: string | null = null;

  constructor(private router: Router) {}

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    
    if (!token) {
      // If there's no token, redirect to sign-in
      this.router.navigate(['/sign-in']);
    } else {
      // Get the user's role from local storage
      this.userRole = localStorage.getItem('role');

      if (this.userRole === 'PRODUCTOWNER') {
        this.router.navigate(['/dashboard/projects']);
      } else if (this.userRole === 'DEVELOPPER') {
        this.router.navigate(['/dashboard/developer/projects']);
      } else {
        this.router.navigate(['/sign-in']);
      }
    }
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role'); // Optionally remove role as well
    this.router.navigate(['/sign-in']);
  }
}
