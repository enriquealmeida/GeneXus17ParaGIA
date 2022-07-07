import { r as registerInstance, h, H as Host } from './index-09b1517f.js';
import { s as state } from './store-f5fbe254.js';
var testCss = ":host{background-color:red}:host(.large){background-color:yellow}";
var GxgTest = /** @class */ (function () {
    function GxgTest(hostRef) {
        registerInstance(this, hostRef);
    }
    GxgTest.prototype.render = function () {
        return (h(Host, { class: {
                large: state.large,
            } }, "hola"));
    };
    return GxgTest;
}());
GxgTest.style = testCss;
export { GxgTest as gxg_test };
