import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

interface Score {
  userId: string;
  score: number;
}

@Component({
  selector: 'app-view-estimation',
  templateUrl: './view-estimation.component.html',
  styleUrls: ['./view-estimation.component.css']
})
export class ViewEstimationComponent implements OnInit {
  scores: Score[] = [];
  estimationId: string | null = '';
  estimation: any = null;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.estimationId = params.get('id');
      if (this.estimationId) {
        this.loadScores(this.estimationId);
      }
    });
  }

  loadScores(estimationId: string): void {

    const token = localStorage.getItem('token');
    if (token) {
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });

    this.http.get<Score[]>(`http://localhost:8080/api/estimations/get/${estimationId}`, {headers})
      .subscribe(data => {
        this.scores = data;
        this.estimation = data
        console.log("estimation: ", this.estimation);
        
      });
    }
  }
}
