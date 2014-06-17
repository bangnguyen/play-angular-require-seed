(function(angular) {
    angular.module('angular.dragdrop', ['ng'])
        /**
         * Applies the jquery-ui draggable plugin to the marked element. Use with
         * 'draggableData' attribute to specify a scope attribute that will be bound
         * to the draggable object.
         *
         * E.g. <... ltp-draggable draggable-data="item" ...> will bind $scope.item to the draggable.
         * The data can then be accessed via element.data('draggable').
         */
        .directive('angularDraggable', ['$parse', function($parse) {
            return {
                link: function($scope, $element, $attrs, $location) {
                    var cursorAt, grid;
                    try {
                        cursorAt = JSON.parse($attrs.cursorAt);
                        grid = JSON.parse($attrs.grid);
                    } catch(e) {
                        // No valid JSON or undefined, that's OK
                    }

                    var draggable = $element.draggable({
                        addClasses: $attrs.addClasses === 'false' ? false : true,
                        appendTo: $attrs.appendTo,
                        axis: $attrs.axis,
                        cancel: $attrs.cancel,
                        connectToSortable: $attrs.connectToSortable,
                        containment: $attrs.containment,
                        cursor: $attrs.cursor,
                        cursorAt: cursorAt,
                        delay: ~~$attrs.delay,
                        disabled: $attrs.disabled === 'true' ? true : false,
                        distance: typeof $attrs.distance !== 'undefined' ? ~~$attrs.distance : undefined,
                        grid: grid,
                        handle: $attrs.handle,
                        helper: $attrs.helper,
                        iframeFix: $attrs.iframeFix === 'true' ? true : $attrs.iframeFix,
                        opacity: $attrs.opacity ? +$attrs.opacity : $attrs.opacity,
                        refreshPositions: $attrs.refreshPositions === 'true' ? true : false,
                        revert: $attrs.revert,
                        revertDuration: typeof $attrs.revertDuration !== 'undefined' ? ~~$attrs.revertDuration : undefined,
                        scope: $attrs.scope,
                        scroll: $attrs.scroll === 'false' ? false : true,
                        scrollSensitivity: typeof $attrs.scrollSensitivity !== 'undefined' ? ~~$attrs.scrollSensitivity : undefined,
                        scrollSpeed: typeof $attrs.scrollSpeed !== 'undefined' ? ~~$attrs.scrollSpeed : undefined,
                        snap: $attrs.snap === 'true' ? true : $attrs.snap,
                        snapMode: $attrs.snapMode,
                        snapTolerance: typeof $attrs.snapTolerance !== 'undefined' ? ~~$attrs.snapTolerance : undefined,
                        stack: $attrs.stack,
                        zIndex: typeof $attrs.zIndex !== 'undefined' ? ~~$attrs.zIndex : undefined
                });
                    if($attrs.draggableData) {
                        draggable.data('angular-draggable', $parse($attrs.draggableData)($scope));
                    }
                }
            };
        }])
        /**
         * Applies the jquery-ui droppable plugin to the marked element. Options:
         * - 'accept': use to specify the classes this widget will accept
         * - 'hoverClass': use to specify the class apply while this element is hovered
         *      by a valid (i.e. 'accept'-ed) draggable
         * - 'onDrop': use to specify the handler to use when a drop is accepted.
         *      Handlers may be any scope expression, i.e. functions or objects.
         *      If the handler is a function and $index is available in the scope,
         *      it will be called as 'handler(draggableData, $index)'.
         *
         *
         * Examples:
         * - <... accept=".class1, .class2" ...>
         *     Droppable accepts draggables of class 'class1' and/or 'class2'
         *
         * - <... hover-class="class3" ...>
         *     'class3' is applied to the droppable element when a valid draggable
         *     hovers over the droppable area
         *
         * - <... on-drop="someFunction" ...>
         *     '$scope.someFunction' is called with the draggable data (if there is any) when
         *     the draggable is dropped
         *
         * - <... on-drop="some.path" ...>
         *     the draggable data is written to '$scope.some.path' when it is dropped. This does
         *     not erase '$scope.some.path' if the draggable data is undefined.
         *
         * - <... on-drop="{".class1": "handler1", "#no2": "handler2"}" ...>
         *     Note: The inner '"' need to be replaced with '&quot;'
         *     if a draggable of class 'class1' is dropped, handler1 is used. If a draggable of id
         *     'no2' is dropped, handler2 is used. If an element with both class 'class1' and id
         *     'no2' is dropped, both handlers are used.
         *
         */
        .directive('angularDroppable', ['$parse', function($parse) {
            return {
                link: function($scope, $element, $attrs, $location) {
                    var directive = {
                        /**
                         * Stores a mapping of css selectors to drop event handler functions,
                         * e.g.
                         *  - '*': 'handleAll'
                         *  - '#myDiv': 'handleMyDiv'
                         *  - '.myCls': 'handleMyCls'
                         */
                        classHandlerMap: {}
                    };

                    try {
                        directive.classHandlerMap = JSON.parse($attrs.onDrop);
                    } catch(e) {
                        directive.classHandlerMap['*'] = $attrs.onDrop;
                    }

                    var accept;

                    try {
                        accept = $parse($attrs.accept)($scope);
                    } catch(e) {
                        accept = $attrs.accept || '*';
                    }

                    /**
                     * Uses the linking function to determine if the receiver of 'data'
                     * is a function or something else. If it is a function, it is going
                     * to be called with 'data' (and '$index', if applicable), otherwise
                     * 'data' is assigned to the scope via 'link'.
                     *
                     * @param {function} link The linking function
                     * @param {any} data The data to pass to the receiver
                     */
                    directive.write = function(link, data) {
                        var handler = link($scope);
                        if(typeof handler === 'function') {
                            if(typeof $scope.$index !== 'undefined') {
                                handler(data, $scope.$index);
                            } else {
                                handler(data);
                            }
                            $scope.$apply();
                        } else {
                            if(typeof link.assign === 'function') {
                                // May be undefined if no handler is specified
                                link.assign($scope, data);
                                $scope.$apply();
                            }
                        }
                    };

                    $element.on('drop', function(event, ui) {
                        var data, isDraggable;
                        if(ui && ui.draggable && typeof ui.draggable.data === 'function') {
                            isDraggable = true;
                            data = ui.draggable.data('angular-draggable');
                        }

                        for(var cls in directive.classHandlerMap) {
                            if(directive.classHandlerMap.hasOwnProperty(cls)) {
                                var handlerName = directive.classHandlerMap[cls];

                                if(isDraggable) {
                                    if(ui.draggable.is(accept) && ui.draggable.is(cls)) {
                                        directive.write($parse(handlerName), data);
                                    }
                                } else {
                                    directive.write($parse(handlerName), data);
                                }
                            }
                        }
                    });

                    $element.droppable({
                        accept: accept,
                        activeClass: $attrs.activeClass,
                        addClasses: $attrs.addClasses === 'false' ? false : true,
                        disabled: $attrs.disabled === 'true' ? true : $attrs.disabled,
                        greedy: $attrs.greedy === 'true' ? true : $attrs.greedy,
                        hoverClass: $attrs.hoverClass,
                        scope: $attrs.scope,
                        tolerance: $attrs.tolerance
                    });
                }
            };
        }]);
}(window.angular));