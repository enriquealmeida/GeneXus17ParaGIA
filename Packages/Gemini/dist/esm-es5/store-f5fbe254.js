import { f as getRenderingRef, i as forceUpdate } from './index-09b1517f.js';
var appendToMap = function (map, propName, value) {
    var items = map.get(propName);
    if (!items) {
        map.set(propName, [value]);
    }
    else if (!items.includes(value)) {
        items.push(value);
    }
};
var debounce = function (fn, ms) {
    var timeoutId;
    return function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i] = arguments[_i];
        }
        if (timeoutId) {
            clearTimeout(timeoutId);
        }
        timeoutId = setTimeout(function () {
            timeoutId = 0;
            fn.apply(void 0, args);
        }, ms);
    };
};
/**
 * Check if a possible element isConnected.
 * The property might not be there, so we check for it.
 *
 * We want it to return true if isConnected is not a property,
 * otherwise we would remove these elements and would not update.
 *
 * Better leak in Edge than to be useless.
 */
var isConnected = function (maybeElement) { return !('isConnected' in maybeElement) || maybeElement.isConnected; };
var cleanupElements = debounce(function (map) {
    for (var _i = 0, _a = map.keys(); _i < _a.length; _i++) {
        var key = _a[_i];
        map.set(key, map.get(key).filter(isConnected));
    }
}, 2000);
var stencilSubscription = function (_a) {
    var on = _a.on;
    var elmsToUpdate = new Map();
    if (typeof getRenderingRef === 'function') {
        // If we are not in a stencil project, we do nothing.
        // This function is not really exported by @stencil/core.
        on('dispose', function () {
            elmsToUpdate.clear();
        });
        on('get', function (propName) {
            var elm = getRenderingRef();
            if (elm) {
                appendToMap(elmsToUpdate, propName, elm);
            }
        });
        on('set', function (propName) {
            var elements = elmsToUpdate.get(propName);
            if (elements) {
                elmsToUpdate.set(propName, elements.filter(forceUpdate));
            }
            cleanupElements(elmsToUpdate);
        });
        on('reset', function () {
            elmsToUpdate.forEach(function (elms) { return elms.forEach(forceUpdate); });
            cleanupElements(elmsToUpdate);
        });
    }
};
var createObservableMap = function (defaultState, shouldUpdate) {
    if (shouldUpdate === void 0) { shouldUpdate = function (a, b) { return a !== b; }; }
    var states = new Map(Object.entries(defaultState !== null && defaultState !== void 0 ? defaultState : {}));
    var handlers = {
        dispose: [],
        get: [],
        set: [],
        reset: [],
    };
    var reset = function () {
        states = new Map(Object.entries(defaultState !== null && defaultState !== void 0 ? defaultState : {}));
        handlers.reset.forEach(function (cb) { return cb(); });
    };
    var dispose = function () {
        // Call first dispose as resetting the state would
        // cause less updates ;)
        handlers.dispose.forEach(function (cb) { return cb(); });
        reset();
    };
    var get = function (propName) {
        handlers.get.forEach(function (cb) { return cb(propName); });
        return states.get(propName);
    };
    var set = function (propName, value) {
        var oldValue = states.get(propName);
        if (shouldUpdate(value, oldValue, propName)) {
            states.set(propName, value);
            handlers.set.forEach(function (cb) { return cb(propName, value, oldValue); });
        }
    };
    var state = (typeof Proxy === 'undefined'
        ? {}
        : new Proxy(defaultState, {
            get: function (_, propName) {
                return get(propName);
            },
            ownKeys: function (_) {
                return Array.from(states.keys());
            },
            getOwnPropertyDescriptor: function () {
                return {
                    enumerable: true,
                    configurable: true,
                };
            },
            has: function (_, propName) {
                return states.has(propName);
            },
            set: function (_, propName, value) {
                set(propName, value);
                return true;
            },
        }));
    var on = function (eventName, callback) {
        handlers[eventName].push(callback);
        return function () {
            removeFromArray(handlers[eventName], callback);
        };
    };
    var onChange = function (propName, cb) {
        var unSet = on('set', function (key, newValue) {
            if (key === propName) {
                cb(newValue);
            }
        });
        var unReset = on('reset', function () { return cb(defaultState[propName]); });
        return function () {
            unSet();
            unReset();
        };
    };
    var use = function () {
        var subscriptions = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            subscriptions[_i] = arguments[_i];
        }
        return subscriptions.forEach(function (subscription) {
            if (subscription.set) {
                on('set', subscription.set);
            }
            if (subscription.get) {
                on('get', subscription.get);
            }
            if (subscription.reset) {
                on('reset', subscription.reset);
            }
        });
    };
    var forceUpdate = function (key) {
        var oldValue = states.get(key);
        handlers.set.forEach(function (cb) { return cb(key, oldValue, oldValue); });
    };
    return {
        state: state,
        get: get,
        set: set,
        on: on,
        onChange: onChange,
        use: use,
        dispose: dispose,
        reset: reset,
        forceUpdate: forceUpdate,
    };
};
var removeFromArray = function (array, item) {
    var index = array.indexOf(item);
    if (index >= 0) {
        array[index] = array[array.length - 1];
        array.length--;
    }
};
var createStore = function (defaultState, shouldUpdate) {
    var map = createObservableMap(defaultState, shouldUpdate);
    stencilSubscription(map);
    return map;
};
var state = createStore({
    large: document.documentElement.classList.contains("gxg-large"),
}).state;
export { state as s };
