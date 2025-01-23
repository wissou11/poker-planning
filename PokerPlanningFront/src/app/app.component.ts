import { Component } from '@angular/core';

import { ProjectService } from './project.service';

import { OnInit } from '@angular/core';
import { Console } from 'console';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  providers: [ ProjectService ]
})
export class AppComponent implements OnInit{
  title = 'PokerPlanningFront';
  projects: any={};

  constructor(private projectService: ProjectService){

  }

  ngOnInit(){
      console.log('On init....');
      this.projectService.getProjects().subscribe((datas) => {
          this.projects=datas;
      })
  }
}
