import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";


export const resetPasswordMatchValidator:ValidatorFn = (control: AbstractControl):ValidationErrors | null => {
    const newPass = control.get('newPass')
    const confirmPass = control.get('confirmPass')

    if(!newPass || !confirmPass) {
        return null
    }

    return newPass.value === confirmPass.value ? null : { passwordMismatch: true }
}