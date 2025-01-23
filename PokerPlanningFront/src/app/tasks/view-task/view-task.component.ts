import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { timer, Observable } from 'rxjs';
import { map, takeWhile } from 'rxjs/operators';

interface Estimation {
  id: string;
  deadline: number;
  averageScore: number;
}

@Component({
  selector: 'app-view-task',
  templateUrl: './view-task.component.html',
  styleUrls: ['./view-task.component.css']
})
export class ViewTaskComponent implements OnInit {
  projectId: string | null = null;
  taskId: string | null = null;
  task: any;
  estimations: Estimation[] = [];
  interval: number | null = null;
  timer: any;
  estimationId: string = "";
  isModalOpen: boolean = false;

  isVoting: boolean = false;

  timeRemaining$: Observable<number>  = new Observable();
  
  @Input() seconds = 300;

  constructor(private route: ActivatedRoute, private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    
    this.projectId = this.route.snapshot.paramMap.get('id');
    this.taskId = this.route.snapshot.paramMap.get('taskid');

    // console.log("yask idtions: ", this.estimations instanceof Array);

    if (this.taskId) {
      this.getEstimations();
    }

    if (this.projectId && this.taskId) {
      const token = localStorage.getItem('token');
      if (token) {
        const headers = new HttpHeaders({
          'Authorization': `Bearer ${token}`
        });
        this.http.get(`http://localhost:8080/projects/${this.projectId}/tasks/${this.taskId}`, { headers })
          .subscribe(
            (data) => {
              this.task = data;
              console.log(this.task);
            },
            (error) => {
              console.error('Error fetching task:', error);
            }
          );
      } else {
        console.error('No token found');
      }
    }
  }

  getEstimations(): void {
    const token = localStorage.getItem('token');
    if (token && this.taskId) {
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });
      this.http.get<Estimation[]>(`http://localhost:8080/api/estimations/task/${this.taskId}`, { headers })
        .subscribe(
          (data: Estimation[]) => {
            this.estimations = data;
            console.log("estimations: ", this.estimations.length);
            
          },
          (error) => {
            console.error('Error fetching estimations:', error);
          }
        );
    } else {
      console.error('No token found or taskId is missing');
    }
  }

  startVote(): void {
    const intervalInput = (document.getElementById('interval') as HTMLInputElement)?.value;
    
    if (intervalInput) {
      this.interval = parseFloat(intervalInput);
      this.seconds = this.interval * 60;
      this.startTimer();
      this.isVoting = true;
    } else {
      console.error('Invalid interval input');
    }
    
    if (this.interval && this.taskId) {
      this.createEstimation()
    }
    
  }
  
  startTimer(): void {
    this.timeRemaining$ = timer(0, 1000).pipe(
      map(n => this.seconds - n),
      takeWhile(n => n >= 0),
      map(n => n * 1000),
    );
  
    this.timeRemaining$.subscribe(timeRemaining => {
      if (timeRemaining <= 0) {
        this.isVoting = false;
        this.updateEstimationStatus();
      }
    });
  }

  createEstimation(): void {
    const token = localStorage.getItem('token');
    if (token) {
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });
      const estimation = { 
        deadline: this.interval,
        taskId: this.taskId,
      };

      this.http.post(`http://localhost:8080/api/estimations/create`, estimation, { headers, responseType: 'text' }).subscribe(
        (response) => {
          let jsonObject  = JSON.parse(response)
          this.estimationId = jsonObject.id
          console.log('Estimation created');
        },
        (error) => {
          console.error('Error updating estimation:', error);
        }
      );
    }
  }
  
  updateEstimationStatus(): void {
    const token = localStorage.getItem('token');
    if (token && this.taskId) {
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });
      const estimation = { 
        status: 'DISABLE' 
      };

      this.http.put(`http://localhost:8080/api/estimations/status/${this.estimationId}`, estimation, { headers }).subscribe(
        () => {
          console.log('Estimation status updated to DISABLE');
          this.getEstimations();
        },
        (error) => {
          console.error('Error updating estimation status:', error);
        }
      );
    }
  }

  viewEstimation(estimationId: string): void {
    this.router.navigate(['/dashboard/estimations/view', estimationId]);
  }
}
