import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { ProductionLineComponent } from './components/production-line/production-line.component';
import { FontAwesomeModule , FaIconLibrary } from '@fortawesome/angular-fontawesome';

// We are extracting the solid icons and the regular icons
import { fas } from '@fortawesome/free-solid-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    ProductionLineComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FontAwesomeModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})


export class AppModule { 

  constructor(library : FaIconLibrary){

    // This function allow us to bind the icons
    // into every component of the website
    library.addIconPacks(fas, far)

  }
  
}
