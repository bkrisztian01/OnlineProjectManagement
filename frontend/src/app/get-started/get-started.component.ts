import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-get-started',
  templateUrl: './get-started.component.html',
  styleUrls: ['./get-started.component.css']
})
export class GetStartedComponent implements OnInit {
  shown: boolean = false;
  showntext1: boolean = false;
  showntext2: boolean = false;
  showntext3: boolean = false;

  constructor(private router:Router) { }

  ngOnInit(): void {

    (async () => { 
      await this.delay(1000);
      this.showntext1=true;
      await this.delay(100);
      this.showntext2=true;
      await this.delay(100);
      this.showntext3=true;

  })();
  }

  delay(ms: number) {
    return new Promise( resolve => setTimeout(resolve, ms) );
    
}
  CurrentDelay(){
    (async () => { 
      // Do something before delay
      this.shown=true;
      console.log('before delay')
      

      await this.delay(2700);

      // Do something after
      console.log('after delay')
      this.router.navigate(['/login'])
  })();
  }

}
