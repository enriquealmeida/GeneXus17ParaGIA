export interface ICommonActionsElement {
  onDoubleclick(eventInfo: any);
  onClick(eventInfo: any);
  onLongclick(eventInfo: any);
  onSwipe(eventInfo: any);
  onSwipeleftAction(eventInfo: any);
  onSwiperightAction(eventInfo: any);
  onSwipedownAction(eventInfo: any);
  onSwipeupAction(eventInfo: any);
  onDragAction(eventInfo: any);
  onDropAction(eventInfo: any);
  onDropAcceptedAction(eventInfo: any);
  onDragCanceledAction(eventInfo: any);

  setClickAction(action: any);
  setDoubleclickAction(action: any);
  setLongclickAction(action: any);
  setSwipeAction(action: any);
  setSwipeleftAction(action: any);
  setSwiperightAction(action: any);
  setSwipedownAction(action: any);
  setSwipeupAction(action: any);
  setDragAction(action: any);
  setDropAction(action: any);
  setDropAcceptedAction(action: any);
  setDragCanceledAction(action: any);

}

let clickPending = false;

export class UICommonActionsElement implements ICommonActionsElement {

  onClickAction = null;
  onDoubleclickAction = null;
  onLongclickAction = null;
  onSwipeAction = null;
  onSwipeleftAction = null;
  onSwiperightAction = null;
  onSwipedownAction = null;
  onSwipeupAction = null;
  onDragAction = null;
  onDropAction = null;
  onDropAcceptedAction = null;
  onDragCanceledAction = null;


  async onDoubleclick(eventInfo: any) {
    eventInfo.stopPropagation();
    if (this.onDoubleclickAction) {
      clickPending = false;
      await this.onDoubleclickAction();
    }
  }

  onClick(eventInfo: any) {
    eventInfo.stopPropagation();
    if (this.onClickAction) {
      clickPending = true;
      setTimeout(() => {
        if (clickPending) {
          this.onClickAction();
        }
      }, 250);
    }
  }

  onLongclick(eventInfo: any) {
    eventInfo.stopPropagation();
    if (this.onLongclick) {
      this.onLongclickAction();
    }
  }

  onSwipe(eventInfo: any) {
    eventInfo.stopPropagation();
    if (this.onSwipeAction) {
      this.onSwipeAction();
    }
  }

  onSwipeleft(eventInfo: any) {
    eventInfo.stopPropagation();
    if (this.onSwipeleftAction) {
      this.onSwipeleftAction();
    }
  }

  onSwiperight(eventInfo: any) {
    eventInfo.stopPropagation();
    if (this.onSwiperightAction) {
      this.onSwiperightAction();
    }
  }

  onSwipeup(eventInfo: any) {
    eventInfo.stopPropagation();
    if (this.onSwipeupAction) {
      this.onSwipeupAction();
    }
  }

  onSwipedown(eventInfo: any) {
    eventInfo.stopPropagation();
    if (this.onSwipedownAction) {
      this.onSwipedownAction();
    }
  }

  onDrag(eventInfo: any) {
    eventInfo.stopPropagation();
    if (this.onDragAction) {
      this.onDragAction();
    }
  }

  onDrop(eventInfo: any) {
    eventInfo.stopPropagation();
    if (this.onDropAction) {
      this.onDropAction();
    }
  }

  onDropAccepted(eventInfo: any) {
    eventInfo.stopPropagation();
    if (this.onDropAcceptedAction) {
      this.onDropAcceptedAction();
    }
  }

  onDragCanceled(eventInfo: any) {
    eventInfo.stopPropagation();
    if (this.onDragCanceledAction) {
      this.onDragCanceledAction();
    }
  }

  setClickAction(action: any) {
    this.onClickAction = action;
  }

  setDoubleclickAction(action: any) {
    this.onDoubleclickAction = action;
  }

  setLongclickAction(action: any) {
    this.onLongclickAction = action;
  }

  setSwipeAction(action: any) {
    this.onSwipeAction = action;
  }

  setSwipeleftAction(action: any) {
    this.onSwipeleftAction = action;
  }

  setSwiperightAction(action: any) {
    this.onSwiperightAction = action;
  }

  setSwipedownAction(action: any) {
    this.onSwipedownAction = action;
  }

  setSwipeupAction(action: any) {
    this.onSwipeupAction = action;
  }

  setDragAction(action: any) {
    this.onDragAction = action;
  }

  setDropAction(action: any) {
    this.onDropAction = action;
  }

  setDropAcceptedAction(action: any) {
    this.onDropAcceptedAction = action;
  }

  setDragCanceledAction(action: any) {
    this.onDragCanceledAction = action;
  }

}

