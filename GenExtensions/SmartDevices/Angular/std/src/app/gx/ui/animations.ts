import { trigger, style, animate, transition } from "@angular/animations";

export function createEnterLeaveAnimation(name: string, visibleHeight: string) {
  return trigger(name, [
    transition(":enter", [
      style({ height: 0, opacity: 0 }),
      animate(".3s ease-out", style({ height: visibleHeight, opacity: 1 })),
    ]),
    transition(":leave", [
      style({ height: visibleHeight, opacity: 1 }),
      animate(".3s ease-in", style({ height: 0, opacity: 0 })),
    ]),
  ]);
}

export function createFadeInOutAnimation(name: string) {
  return trigger(name, [
    transition(":enter", [
      style({ opacity: 0 }),
      animate(".3s ease-out", style({ opacity: 1 })),
    ]),
    transition(":leave", [
      style({ opacity: 1 }),
      animate(".3s ease-in", style({ opacity: 0 })),
    ]),
  ]);
}
