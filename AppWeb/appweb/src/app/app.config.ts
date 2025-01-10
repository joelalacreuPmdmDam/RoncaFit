import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

import { initializeApp, provideFirebaseApp } from '@angular/fire/app';
import { provideFirestore, getFirestore } from '@angular/fire/firestore';

const firebaseConfig = {
  apiKey: "AIzaSyBCGgUa7sG8eJg5Pb1gjiofZXRckv49zS0",
  authDomain: "roncafit.firebaseapp.com",
  projectId: "roncafit",
  storageBucket: "roncafit.firebasestorage.app",
  messagingSenderId: "802179405053",
  appId: "1:802179405053:web:43d5f6c094a35707335a94"
};

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideClientHydration(),
    provideHttpClient(),
    provideAnimationsAsync(),
    provideFirebaseApp(() => initializeApp(firebaseConfig)), // Inicializar Firebase
    provideFirestore(() => getFirestore()) // Proveedor de Firestore
  ]
};
