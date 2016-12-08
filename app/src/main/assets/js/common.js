/**
 * 自适应布局
 * common.js by mq_brandon
 * lastest: 2016-9-1 10:50:09
**/

(function (doc, win) {
    var docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        psdWidth = 750,  // 设计图宽度
        recalc = function () {
            var clientWidth = docEl.clientWidth;
            if ( !clientWidth ) return;
            if ( clientWidth >= 640 ) {
                docEl.style.fontSize = 100 * ( 640 / psdWidth ) + 'px';
            } else {
                docEl.style.fontSize = 100 * ( clientWidth / psdWidth ) + 'px';
            }
        };

    if ( !doc.addEventListener ) return;
    win.addEventListener( resizeEvt, recalc, false );
    doc.addEventListener( 'DOMContentLoaded', recalc, false );
})(document, window);