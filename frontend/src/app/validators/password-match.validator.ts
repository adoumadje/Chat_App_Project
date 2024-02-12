import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export const passwordMatchValidator:ValidatorFn = (control:AbstractControl): ValidationErrors | null => {
    const password = control.get('password')
    const confirmPass = control.get('confirmPass')
    if(!password || !confirmPass) {
        return null
    }
    
    return password.value === confirmPass.value ? null : { passwordMismatch: true }
}