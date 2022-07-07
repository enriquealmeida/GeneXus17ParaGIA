import { r as registerInstance, h, H as Host } from './index-09b1517f.js';
import { s as state } from './store-f5fbe254.js';

const testCss = ":host{background-color:red}:host(.large){background-color:yellow}";

const GxgTest = class {
    constructor(hostRef) {
        registerInstance(this, hostRef);
    }
    render() {
        return (h(Host, { class: {
                large: state.large,
            } }, "hola"));
    }
};
GxgTest.style = testCss;

export { GxgTest as gxg_test };
