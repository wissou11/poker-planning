import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SignUpComponent } from './sign-up/sign-up.component';
import { SignInComponent } from './sign-in/sign-in.component';
import { TemplatePOComponent } from './template-po/template-po.component';
import { TemplateAdminComponent } from './template-admin/template-admin.component';
import { ProjectsComponent } from './projects/projects.component';
import { TasksComponent } from './tasks/tasks.component';
import { DashboardLayoutComponent } from './dashboard-layout/dashboard-layout.component';
import { CreateProjectComponent } from './projects/create-project/create-project.component';
import { UpdateProjectComponent } from './projects/update-project/update-project.component';
import { ViewProjectComponent } from './projects/view-project/view-project.component';
import { CreateTaskComponent } from './tasks/create-task/create-task.component';
import { UpdateTaskComponent } from './tasks/update-task/update-task.component';
import { ViewTaskComponent } from './tasks/view-task/view-task.component';
import { EstimationsComponent } from './estimations/estimations.component';
import { CommentsComponent } from './comments/comments.component';
import { ViewEstimationComponent } from './estimations/view-estimation/view-estimation.component';
import { DeveloperProjectsComponent } from './developer-projects/developer-projects.component';
import { DeveloperTasksComponent } from './developer-tasks/developer-tasks.component';
import { ViewDevProjectComponent } from './developer-projects/view-dev-project/view-dev-project.component';
import { RedditComponent } from './reddit/reddit.component';

const routes: Routes = [
  { path: '', redirectTo: 'sign-in', pathMatch: 'full' },
  { path: 'sign-up', component: SignUpComponent },
  { path: 'sign-in', component: SignInComponent },
  {
    path: 'dashboard',
    component: DashboardLayoutComponent,
    children: [
      { path: 'projects', component: ProjectsComponent },
      { path: 'projects/create', component: CreateProjectComponent },
      { path: 'projects/update/:id', component: UpdateProjectComponent },
      { path: 'projects/view/:id', component: ViewProjectComponent },
      { path: 'tasks', component: TasksComponent },
      { path: 'projects/view/:id/tasks/create', component: CreateTaskComponent },
      { path: 'projects/view/:id/tasks/update/:taskid', component: UpdateTaskComponent },
      { path: 'projects/view/:id/tasks/view/:taskid', component: ViewTaskComponent },
      { path: 'estimations', component: EstimationsComponent },
      { path: 'estimations/view/:id', component: ViewEstimationComponent },
      { path: 'productowner-dashboard', component: TemplatePOComponent },
      { path: 'developer-dashboard', component: TemplateAdminComponent },
      { path: 'projects/view/:id/tasks/view/:taskid/comments', component: CommentsComponent },
      { path: 'developer/projects', component: DeveloperProjectsComponent },
      { path: 'developer/projects/view/:id', component: ViewDevProjectComponent },
      { path: 'developer/projects/view/:id/tasks/view/:taskid', component: DeveloperTasksComponent },
      { path: 'reddit', component: RedditComponent },
    ],
  },
  // { path: '**', redirectTo: '/sign-in' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
