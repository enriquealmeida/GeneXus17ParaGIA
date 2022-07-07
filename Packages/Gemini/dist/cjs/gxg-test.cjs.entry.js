'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

const index = require('./index-1115561c.js');
const store = require('./store-49c65768.js');

const testCss = ":host{background-color:red}:host(.large){background-color:yellow}";

const GxgTest = class {
    constructor(hostRef) {
        index.registerInstance(this, hostRef);
    }
    render() {
        return (index.h(index.Host, { class: {
                large: store.state.large,
            } }, "hola"));
    }
};
GxgTest.style = testCss;

exports.gxg_test = GxgTest;
