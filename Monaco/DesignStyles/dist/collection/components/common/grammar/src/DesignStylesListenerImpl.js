import { DesignStylesListener } from '../generated/grammars';
export class DesignStylesListenerImpl {
    constructor(res) {
        DesignStylesListener.call(this);
        this.res = res;
    }
    enterDesignStyles(ctx) {
        console.log(ctx);
        this.classes = [];
    }
    exitClass_name(ctx, newClass) {
        if (!this.classes.includes(newClass))
            this.classes.push(ctx.getText().trim());
    }
    ;
    getClasses(ctx) {
        console.log(ctx);
        return this.classes;
    }
    ;
}
