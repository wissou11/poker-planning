import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-estimations',
  templateUrl: './estimations.component.html',
  styleUrls: ['./estimations.component.css']
})
export class EstimationsComponent implements OnInit {
  estimations: any[] = []; // To store fetched estimations
  selectedCard: HTMLElement | null = null;
  selectedScore: number | null = null;
  selectedEstimationId: string | null = null;
  isModalOpen: boolean = false; // Control modal visibility

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const role = localStorage.getItem('role');

    // Check if the user is a DEVELOPPER
    if (role === 'DEVELOPPER') {
      this.fetchEstimations();
    } else {
      console.error('Unauthorized: Only DEVELOPERS can view estimations');
      // Optionally, you can navigate the user to another page or show a message
    }
  }

  fetchEstimations(): void {
    const token = localStorage.getItem('token');
    if (token) {
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });
  
      this.http.get<any[]>('http://localhost:8080/api/estimations/status/enable', { headers })
        .subscribe(data => {
          this.estimations = data;
          console.log('Fetched estimations:', this.estimations);
  
          // Fetch task descriptions
          this.estimations.forEach(estimation => {
            this.http.get<any>(`http://localhost:8080/api/estimations/tasks/${estimation.taskId}`, { headers })
              .subscribe(task => {
                estimation.taskTitle = task.title; // Ajouter le titre de la tâche à l'estimation
              }, error => {
                console.error('Error fetching task title:', error);
              });
          });
        }, error => {
          console.error('Error fetching estimations:', error);
        });
    } else {
      console.error('No token found');
    }
  }
  

  openModal(estimationId: string): void {
    console.log('Opening modal for estimation ID:', estimationId); // Vérifiez l'ouverture du modal
    this.selectedEstimationId = estimationId;
    this.isModalOpen = true; // Show modal
  }

  closeModal(): void {
    this.isModalOpen = false; // Hide modal
  }

  selectCard(score: number, event: Event): void {
    const target = event.currentTarget as HTMLElement;

    if (this.selectedCard) {
      this.selectedCard.classList.remove('card-selected');
    }

    // Select the new card
    target.classList.add('card-selected');
    this.selectedCard = target;
    this.selectedScore = score;
    console.log('Selected score:', this.selectedScore);
  }

  submitEstimation(): void {
    if (!this.selectedEstimationId) {
      alert('No estimation selected.');
      return;
    }

    if (this.selectedScore === null) {
      alert('Please select a score.');
      return;
    }

    const token = localStorage.getItem('token');
    if (token) {
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });

      const score = {
        userId: localStorage?.getItem('user'), // Remplacez par l'ID réel de l'utilisateur
        scoreValue: this.selectedScore,
        estimationId: this.selectedEstimationId
      };

      console.log('Submitting score:', score); // Vérifiez les données envoyées

      this.http.post('http://localhost:8080/api/scores/submit', score, { headers, responseType: 'text' })
        .subscribe(
          () => {
            alert('Score submitted successfully!');
            this.closeModal();
          },
          (error) => {
            if(error.status == 409) alert('Error submitting score: user already submitted');
            this.closeModal(); 
          }
        );
    } else {
      console.error('No token found');
    }
  }
}
