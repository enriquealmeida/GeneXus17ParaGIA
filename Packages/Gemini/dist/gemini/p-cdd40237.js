import { f as e, i as t } from "./p-eb9dc661.js";

const isConnected = e => !("isConnected" in e) || e.isConnected, s = ((e, t) => {
  let s;
  return (...n) => {
    s && clearTimeout(s), s = setTimeout(() => {
      s = 0, e(...n);
    }, t);
  };
})(e => {
  for (let t of e.keys()) e.set(t, e.get(t).filter(isConnected));
}, 2e3), removeFromArray = (e, t) => {
  const s = e.indexOf(t);
  s >= 0 && (e[s] = e[e.length - 1], e.length--);
}, {state: n} = ((n, o) => {
  const r = ((e, t = ((e, t) => e !== t)) => {
    let s = new Map(Object.entries(null != e ? e : {}));
    const n = {
      dispose: [],
      get: [],
      set: [],
      reset: []
    }, reset = () => {
      s = new Map(Object.entries(null != e ? e : {})), n.reset.forEach(e => e());
    }, get = e => (n.get.forEach(t => t(e)), s.get(e)), set = (e, o) => {
      const r = s.get(e);
      t(o, r, e) && (s.set(e, o), n.set.forEach(t => t(e, o, r)));
    }, o = "undefined" == typeof Proxy ? {} : new Proxy(e, {
      get: (e, t) => get(t),
      ownKeys: e => Array.from(s.keys()),
      getOwnPropertyDescriptor: () => ({
        enumerable: !0,
        configurable: !0
      }),
      has: (e, t) => s.has(t),
      set: (e, t, s) => (set(t, s), !0)
    }), on = (e, t) => (n[e].push(t), () => {
      removeFromArray(n[e], t);
    });
    return {
      state: o,
      get,
      set,
      on,
      onChange: (t, s) => {
        const n = on("set", (e, n) => {
          e === t && s(n);
        }), o = on("reset", () => s(e[t]));
        return () => {
          n(), o();
        };
      },
      use: (...e) => e.forEach(e => {
        e.set && on("set", e.set), e.get && on("get", e.get), e.reset && on("reset", e.reset);
      }),
      dispose: () => {
        // Call first dispose as resetting the state would
        // cause less updates ;)
        n.dispose.forEach(e => e()), reset();
      },
      reset,
      forceUpdate: e => {
        const t = s.get(e);
        n.set.forEach(s => s(e, t, t));
      }
    };
  })(n, o);
  return (({on: n}) => {
    const o = new Map;
    "function" == typeof e && (
    // If we are not in a stencil project, we do nothing.
    // This function is not really exported by @stencil/core.
    n("dispose", () => {
      o.clear();
    }), n("get", t => {
      const s = e();
      s && ((e, t, s) => {
        const n = e.get(t);
        n ? n.includes(s) || n.push(s) : e.set(t, [ s ]);
      })(o, t, s);
    }), n("set", e => {
      const n = o.get(e);
      n && o.set(e, n.filter(t)), s(o);
    }), n("reset", () => {
      o.forEach(e => e.forEach(t)), s(o);
    }));
  })(r), r;
})({
  large: document.documentElement.classList.contains("gxg-large")
});

export { n as s }