const e="gxcf-conversational-flows";let n,t,l=0,o=!1;const s="undefined"!=typeof window?window:{},c=s.CSS,r=s.document||{head:{}},i={t:0,l:"",jmp:e=>e(),raf:e=>requestAnimationFrame(e),ael:(e,n,t,l)=>e.addEventListener(n,t,l),rel:(e,n,t,l)=>e.removeEventListener(n,t,l),ce:(e,n)=>new CustomEvent(e,n)},a=(()=>(r.head.attachShadow+"").indexOf("[native")>-1)(),u=e=>Promise.resolve(e),f=(()=>{try{return new CSSStyleSheet,!0}catch(e){}return!1})(),d=(e,n,t)=>{t&&t.map(([t,l,o])=>{const s=e,c=p(n,o),r=m(t);i.ael(s,l,c,r),(n.o=n.o||[]).push(()=>i.rel(s,l,c,r))})},p=(e,n)=>t=>{256&e.t?e.s[n](t):(e.i=e.i||[]).push([n,t])},m=e=>0!=(2&e),y=new WeakMap,$=e=>"sc-"+e.u,h={},b=e=>"object"==(e=typeof e)||"function"===e,w="undefined"!=typeof Deno,g=!(w||"undefined"==typeof global||"function"!=typeof require||!global.process||"string"!=typeof __filename||global.origin&&"string"==typeof global.origin),_=(w&&Deno,g?process:w&&Deno,g?process:w&&Deno,(e,n,...t)=>{let l=null,o=!1,s=!1,c=[];const r=n=>{for(let t=0;t<n.length;t++)l=n[t],Array.isArray(l)?r(l):null!=l&&"boolean"!=typeof l&&((o="function"!=typeof e&&!b(l))&&(l+=""),o&&s?c[c.length-1].p+=l:c.push(o?v(null,l):l),s=o)};if(r(t),n){const e=n.className||n.class;e&&(n.class="object"!=typeof e?e:Object.keys(e).filter(n=>e[n]).join(" "))}const i=v(e,null);return i.m=n,c.length>0&&(i.$=c),i}),v=(e,n)=>({t:0,h:e,p:n,g:null,$:null,m:null}),j={},M=(e,n,t,l,o,c)=>{if(t!==l){let a=Z(e,n),u=n.toLowerCase();if("class"===n){const n=e.classList,o=x(t),s=x(l);n.remove(...o.filter(e=>e&&!s.includes(e))),n.add(...s.filter(e=>e&&!o.includes(e)))}else if("style"===n){for(const n in t)l&&null!=l[n]||(n.includes("-")?e.style.removeProperty(n):e.style[n]="");for(const n in l)t&&l[n]===t[n]||(n.includes("-")?e.style.setProperty(n,l[n]):e.style[n]=l[n])}else if(a||"o"!==n[0]||"n"!==n[1]){const s=b(l);if((a||s&&null!==l)&&!o)try{if(e.tagName.includes("-"))e[n]=l;else{let o=null==l?"":l;"list"===n?a=!1:null!=t&&e[n]==o||(e[n]=o)}}catch(r){}null==l||!1===l?!1===l&&""!==e.getAttribute(n)||e.removeAttribute(n):(!a||4&c||o)&&!s&&e.setAttribute(n,l=!0===l?"":l)}else n="-"===n[2]?n.slice(3):Z(s,u)?u.slice(2):u[2]+n.slice(3),t&&i.rel(e,n,t,!1),l&&i.ael(e,n,l,!1)}},S=/\s/,x=e=>e?e.split(S):[],D=(e,n,t,l)=>{const o=11===n.g.nodeType&&n.g.host?n.g.host:n.g,s=e&&e.m||h,c=n.m||h;for(l in s)l in c||M(o,l,s[l],void 0,t,n.t);for(l in c)M(o,l,s[l],c[l],t,n.t)},O=(e,t,l)=>{let o,s,c=t.$[l],i=0;if(null!==c.p)o=c.g=r.createTextNode(c.p);else if(o=c.g=r.createElement(c.h),D(null,c,!1),null!=n&&o["s-si"]!==n&&o.classList.add(o["s-si"]=n),c.$)for(i=0;i<c.$.length;++i)s=O(e,c,i),s&&o.appendChild(s);return o},k=(e,n,l,o,s,c)=>{let r,i=e;for(i.shadowRoot&&i.tagName===t&&(i=i.shadowRoot);s<=c;++s)o[s]&&(r=O(null,l,s),r&&(o[s].g=r,i.insertBefore(r,n)))},C=(e,n,t,l)=>{for(;n<=t;++n)(l=e[n])&&l.g.remove()},R=(e,n)=>e.h===n.h,L=(e,n)=>{const t=n.g=e.g,l=e.$,o=n.$,s=n.p;null===s?(D(e,n,!1),null!==l&&null!==o?((e,n,t,l)=>{let o,s=0,c=0,r=n.length-1,i=n[0],a=n[r],u=l.length-1,f=l[0],d=l[u];for(;s<=r&&c<=u;)null==i?i=n[++s]:null==a?a=n[--r]:null==f?f=l[++c]:null==d?d=l[--u]:R(i,f)?(L(i,f),i=n[++s],f=l[++c]):R(a,d)?(L(a,d),a=n[--r],d=l[--u]):R(i,d)?(L(i,d),e.insertBefore(i.g,a.g.nextSibling),i=n[++s],d=l[--u]):R(a,f)?(L(a,f),e.insertBefore(a.g,i.g),a=n[--r],f=l[++c]):(o=O(n&&n[c],t,c),f=l[++c],o&&i.g.parentNode.insertBefore(o,i.g));s>r?k(e,null==l[u+1]?null:l[u+1].g,t,l,c,u):c>u&&C(n,s,r)})(t,l,n,o):null!==o?(null!==e.p&&(t.textContent=""),k(t,null,n,o,0,o.length-1)):null!==l&&C(l,0,l.length-1)):e.p!==s&&(t.data=s)},P=e=>Q(e)._,U=(e,n,t)=>{const l=P(e);return{emit:e=>W(l,n,{bubbles:!!(4&t),composed:!!(2&t),cancelable:!!(1&t),detail:e})}},W=(e,n,t)=>{const l=i.ce(n,t);return e.dispatchEvent(l),l},E=(e,n)=>{n&&!e.v&&n["s-p"]&&n["s-p"].push(new Promise(n=>e.v=n))},T=(e,n)=>{if(e.t|=16,!(4&e.t))return E(e,e.j),fe(()=>q(e,n));e.t|=512},q=(e,n)=>{const t=e.s;let l;return n&&(e.t|=256,e.i&&(e.i.map(([e,n])=>V(t,e,n)),e.i=null),l=V(t,"componentWillLoad")),l=z(l,()=>V(t,"componentWillRender")),z(l,()=>A(e,t,n))},A=(e,l,o)=>{const s=e._,c=s["s-rc"];o&&(e=>{const n=e.M,t=e._,l=n.t,o=((e,n)=>{let t=$(n),l=le.get(t);if(e=11===e.nodeType?e:r,l)if("string"==typeof l){let n,o=y.get(e=e.head||e);o||y.set(e,o=new Set),o.has(t)||(n=r.createElement("style"),n.innerHTML=l,e.insertBefore(n,e.querySelector("link")),o&&o.add(t))}else e.adoptedStyleSheets.includes(l)||(e.adoptedStyleSheets=[...e.adoptedStyleSheets,l]);return t})(a&&t.shadowRoot?t.shadowRoot:t.getRootNode(),n);10&l&&(t["s-sc"]=o,t.classList.add(o+"-h"))})(e);((e,l)=>{const o=e._,s=e.S||v(null,null),c=(e=>e&&e.h===j)(l)?l:_(null,null,l);t=o.tagName,c.h=null,c.t|=4,e.S=c,c.g=s.g=o.shadowRoot||o,n=o["s-sc"],L(s,c)})(e,F(e,l)),c&&(c.map(e=>e()),s["s-rc"]=void 0);{const n=s["s-p"],t=()=>N(e);0===n.length?t():(Promise.all(n).then(t),e.t|=4,n.length=0)}},F=(e,n)=>{try{n=n.render(),e.t&=-17,e.t|=2}catch(t){ee(t)}return n},N=e=>{const n=e._,t=e.s,l=e.j;V(t,"componentDidRender"),64&e.t?V(t,"componentDidUpdate"):(e.t|=64,B(n),V(t,"componentDidLoad"),e.D(n),l||H()),e.O(n),e.v&&(e.v(),e.v=void 0),512&e.t&&ue(()=>T(e,!1)),e.t&=-517},H=()=>{B(r.documentElement),i.t|=2,ue(()=>W(s,"appload",{detail:{namespace:"gxcf-conversational-flows"}}))},V=(e,n,t)=>{if(e&&e[n])try{return e[n](t)}catch(l){ee(l)}},z=(e,n)=>e&&e.then?e.then(n):n(),B=e=>e.classList.add("hydrated"),G=(e,n,t)=>{if(n.k){const l=Object.entries(n.k),o=e.prototype;if(l.map(([e,[l]])=>{31&l||2&t&&32&l?Object.defineProperty(o,e,{get(){return((e,n)=>Q(this).C.get(n))(0,e)},set(t){((e,n,t,l)=>{const o=Q(e),s=o.C.get(n),c=o.t,r=o.s;t=((e,n)=>null==e||b(e)?e:4&n?"false"!==e&&(""===e||!!e):2&n?parseFloat(e):1&n?e+"":e)(t,l.k[n][0]),8&c&&void 0!==s||t===s||(o.C.set(n,t),r&&2==(18&c)&&T(o,!1))})(this,e,t,n)},configurable:!0,enumerable:!0}):1&t&&64&l&&Object.defineProperty(o,e,{value(...n){const t=Q(this);return t.R.then(()=>t.s[e](...n))}})}),1&t){const n=new Map;o.attributeChangedCallback=function(e,t,l){i.jmp(()=>{const t=n.get(e);this[t]=(null!==l||"boolean"!=typeof this[t])&&l})},e.observedAttributes=l.filter(([e,n])=>15&n[0]).map(([e,t])=>{const l=t[1]||e;return n.set(l,e),l})}}return e},I=(e,n={})=>{const t=[],l=n.exclude||[],o=s.customElements,c=r.head,u=c.querySelector("meta[charset]"),p=r.createElement("style"),m=[];let y,h=!0;Object.assign(i,n),i.l=new URL(n.resourcesUrl||"./",r.baseURI).href,n.syncQueue&&(i.t|=4),e.map(e=>e[1].map(n=>{const s={t:n[0],u:n[1],k:n[2],L:n[3]};s.k=n[2],s.L=n[3],!a&&1&s.t&&(s.t|=8);const c=s.u,r=class extends HTMLElement{constructor(e){super(e),Y(e=this,s),1&s.t&&(a?e.attachShadow({mode:"open"}):"shadowRoot"in e||(e.shadowRoot=e))}connectedCallback(){y&&(clearTimeout(y),y=null),h?m.push(this):i.jmp(()=>(e=>{if(0==(1&i.t)){const n=Q(e),t=n.M,l=()=>{};if(1&n.t)d(e,n,t.L);else{n.t|=1;{let t=e;for(;t=t.parentNode||t.host;)if(t["s-p"]){E(n,n.j=t);break}}t.k&&Object.entries(t.k).map(([n,[t]])=>{if(31&t&&e.hasOwnProperty(n)){const t=e[n];delete e[n],e[n]=t}}),ue(()=>(async(e,n,t,l,o)=>{if(0==(32&n.t)){{if(n.t|=32,(o=te(t)).then){const e=()=>{};o=await o,e()}o.isProxied||(G(o,t,2),o.isProxied=!0);const e=()=>{};n.t|=8;try{new o(n)}catch(r){ee(r)}n.t&=-9,e()}if(o.style){let e=o.style;const n=$(t);if(!le.has(n)){const l=()=>{};8&t.t&&(e=await __sc_import_gxcf_conversational_flows("./p-4b7cd42f.js").then(t=>t.scopeCss(e,n,!1))),((e,n,t)=>{let l=le.get(e);f&&t?(l=l||new CSSStyleSheet,l.replace(n)):l=n,le.set(e,l)})(n,e,!!(1&t.t)),l()}}}const s=n.j,c=()=>T(n,!0);s&&s["s-rc"]?s["s-rc"].push(c):c()})(0,n,t))}l()}})(this))}disconnectedCallback(){i.jmp(()=>(()=>{if(0==(1&i.t)){const e=Q(this);e.o&&(e.o.map(e=>e()),e.o=void 0)}})())}forceUpdate(){(()=>{{const e=Q(this);e._.isConnected&&2==(18&e.t)&&T(e,!1)}})()}componentOnReady(){return Q(this).P}};s.U=e[0],l.includes(c)||o.get(c)||(t.push(c),o.define(c,G(r,s,1)))})),p.innerHTML=t+"{visibility:hidden}.hydrated{visibility:inherit}",p.setAttribute("data-styles",""),c.insertBefore(p,u?u.nextSibling:c.firstChild),h=!1,m.length?m.map(e=>e.connectedCallback()):i.jmp(()=>y=setTimeout(H,30))},J=e=>{const n=new URL(e,i.l);return n.origin!==s.location.origin?n.href:n.pathname},K=new WeakMap,Q=e=>K.get(e),X=(e,n)=>K.set(n.s=e,n),Y=(e,n)=>{const t={t:0,_:e,M:n,C:new Map};return t.R=new Promise(e=>t.O=e),t.P=new Promise(e=>t.D=e),e["s-p"]=[],e["s-rc"]=[],d(e,t,n.L),K.set(e,t)},Z=(e,n)=>n in e,ee=e=>console.error(e),ne=new Map,te=e=>{const n=e.u.replace(/-/g,"_"),t=e.U,l=ne.get(t);return l?l[n]:__sc_import_gxcf_conversational_flows(`./${t}.entry.js`).then(e=>(ne.set(t,e),e[n]),ee)},le=new Map,oe=[],se=[],ce=[],re=(e,n)=>t=>{e.push(t),o||(o=!0,n&&4&i.t?ue(ae):i.raf(ae))},ie=(e,n)=>{let t=0,l=0;for(;t<e.length&&(l=performance.now())<n;)try{e[t++](l)}catch(o){ee(o)}t===e.length?e.length=0:0!==t&&e.splice(0,t)},ae=()=>{l++,(e=>{for(let t=0;t<e.length;t++)try{e[t](performance.now())}catch(n){ee(n)}e.length=0})(oe);{const e=2==(6&i.t)?performance.now()+14*Math.ceil(.1*l):1/0;ie(se,e),ie(ce,e),se.length>0&&(ce.push(...se),se.length=0),(o=oe.length+se.length+ce.length>0)?i.raf(ae):l=0}},ue=e=>u().then(e),fe=re(se,!0);export{c as C,e as N,u as a,I as b,U as c,r as d,J as e,P as g,_ as h,i as p,X as r,s as w}