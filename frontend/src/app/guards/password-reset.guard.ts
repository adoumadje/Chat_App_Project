import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const passwordResetGuard: CanActivateFn = (route, state) => {
   if(sessionStorage.getItem('passwordResetToken')) {
    return true
   } else {
    const router = inject(Router)
    return router.navigate(['login'])
   }
};
