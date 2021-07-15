import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// Importing whatever component we want to add to the routers
import { ProductionLineComponent } from './components/production-line/production-line.component';

const routes: Routes = [
  {path: 'fruits', component: ProductionLineComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }
