import { Component, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { Navbar } from './core/components/navbar/navbar';
import { Auth } from './core/services/auth';

@Component({
  selector: 'app-root',
  imports: [CommonModule, RouterOutlet, Navbar],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('frontend');
  showNavbar = signal(false);
  
  constructor(
    private router: Router,
    private authService: Auth
  ) {
    // Update navbar visibility on route changes
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        this.updateNavbarVisibility();
      });
    
    // Initial check
    this.updateNavbarVisibility();
  }

  private updateNavbarVisibility(): void {
    const currentUrl = this.router.url;
    const isAuthPage = currentUrl.includes('/login') || currentUrl.includes('/register');
    const isLoggedIn = !!this.authService.getCurrentUser();
    
    this.showNavbar.set(isLoggedIn && !isAuthPage);
  }
}
