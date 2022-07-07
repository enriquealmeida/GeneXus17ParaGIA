/// <reference types="react" />
interface FormComponent {
    error: boolean;
    isRequiredError: boolean;
    requiredMessage: string;
}
declare function requiredLabel(component: any): any;
declare function formMessage(children?: JSX.Element): any;
declare function formHandleChange(component: FormComponent, formElement: HTMLInputElement | HTMLTextAreaElement): void;
export { requiredLabel, formHandleChange, formMessage, FormComponent };
