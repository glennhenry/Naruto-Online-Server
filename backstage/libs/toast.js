let warned = false;
const TOAST_HEIGHT_LIMIT = 400;

/**
 * Provides API to produce toast messages, a short informational text
 * that is displayed on the screen to inform user of certain condition.
 *
 * Available levels: {@link success}, {@link info}, {@link warn}, {@link error}.
 *
 * Each levels takes:
 * - an optional title.
 * - a required message string
 * - a boolean value on whether the toast message should be closed
 *   automatically after 5 seconds. Default is `false` — toast stay
 *   still until the user close it manually or toast limit is exceeded.
 *   Use `false` if the toast is important.
 */
class Toast {
  static success(title = "A successful message", msg, autoClose = false) {
    const container = $(".toast-container");
    if (!this.#warnIfNotExist(container)) return;

    this.#addToast(this.#newToast(1, title, msg), autoClose);
  }

  static info(title = "An informational message", msg, autoClose = false) {
    const container = $(".toast-container");
    if (!this.#warnIfNotExist(container)) return;

    this.#addToast(this.#newToast(2, title, msg), autoClose);
  }

  static warn(title = "A warning message", msg, autoClose = false) {
    const container = $(".toast-container");
    if (!this.#warnIfNotExist(container)) return;

    this.#addToast(this.#newToast(3, title, msg), autoClose);
  }

  static error(title = "An error message", msg, autoClose = false) {
    const container = $(".toast-container");
    if (!this.#warnIfNotExist(container)) return;

    this.#addToast(this.#newToast(4, title, msg), autoClose);
  }

  static #warnIfNotExist(container) {
    if (!container.length && !warned) {
      console.warn(
        "Toast message could not be enabled because element '.toast-container' does not exist.",
      );
      warned = true;
      return false;
    }
    return true;
  }

  static #newToast(level, title, msg) {
    const toastElement = $("<div></div>").addClass("toast");

    const toastClose = $("<a></a>")
      .addClass("toast-close")
      .append(
        $("<img>").attr("src", "/backstage/assets/close.png").attr("title", "Dismiss"),
      );
    const toastIcon = $("<img>").addClass("toast-icon");
    const toastTitle = $("<p></p>").text(title).addClass("toast-title");
    const toastMsg = $("<p></p>").text(msg).addClass("toast-text");
    const toastContent = $("<div></div>").append(toastTitle).append(toastMsg);

    if (level == 1) {
      toastIcon.attr("src", "/backstage/assets/success.png");
      toastElement.addClass("toast-success");
    } else if (level == 2) {
      toastIcon.attr("src", "/backstage/assets/info.png");
      toastElement.addClass("toast-info");
    } else if (level == 3) {
      toastIcon.attr("src", "/backstage/assets/warn.png");
      toastElement.addClass("toast-warn");
    } else if (level == 4) {
      toastIcon.attr("src", "/backstage/assets/error.png");
      toastElement.addClass("toast-error");
    } else {
      console.warn("Unexpected level: ", level);
    }

    const toastId = crypto.randomUUID();
    toastElement
      .append(toastClose)
      .append(toastIcon)
      .append(toastContent)
      .attr("data-toast-id", toastId)
      .click(function () {
        Toast.#deleteToast(toastId);
      });
    return toastElement;
  }

  static #addToast(toastElement, autoClose) {
    const container = $(".toast-container");
    container.append(toastElement);

    setTimeout(() => {
      toastElement.addClass("toast-show");
    }, 10);

    const getTotalHeight = () => {
      let total = 0;
      container.children().each(function () {
        total += this.offsetHeight;
      });
      return total;
    };

    while (getTotalHeight() > TOAST_HEIGHT_LIMIT) {
      const firstToast = container.children().first();
      if (firstToast.length === 0) break;
      firstToast.remove();
    }

    if (autoClose) {
      setTimeout(() => {
        this.#deleteToast(toastElement.attr("data-toast-id"));
      }, 5000);
    }
  }

  static #deleteToast(toastId) {
    const toastElement = $(".toast-container").children(
      `.toast[data-toast-id=${toastId}]`,
    );

    if (!toastElement.length) return;

    toastElement.removeClass("toast-show");
    toastElement.on("transitionend", function () {
      toastElement.remove();
    });
  }
}
