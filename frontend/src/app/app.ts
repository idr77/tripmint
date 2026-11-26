import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly appName = signal('TripMint');
  protected readonly tagline = signal(
    'Optimiseur de voyages multi-critères à bas coût (dates fixes & flexibles)',
  );
}
