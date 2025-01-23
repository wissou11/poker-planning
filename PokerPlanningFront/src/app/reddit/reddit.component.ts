import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reddit',
  templateUrl: './reddit.component.html',
  styleUrl: './reddit.component.css'
})
export class RedditComponent implements OnInit {
  reddits: any;
  constructor(private http: HttpClient, private router: Router) { }

  isExpanded: { [key: number]: boolean } = {};

  toggleViewMore(index: number): void {
    this.isExpanded[index] = !this.isExpanded[index];
  }
  ngOnInit() {
    try {
        this.http.get("http://localhost:5000/data").subscribe(
          (data) => {
            this.reddits = data;
            console.log('Reddits fetched successfully:', data);
          },
          (error) => {
            console.error('Error fetching reddits:', error);
            if (error.status === 401) {
              console.error('Unauthorized - Invalid or expired token');
            } else {
              console.error('Error status:', error.status);
            }
          }
        );
      }
     catch (error) {
      console.error('An error occurred:', error);
    }
  }
}
