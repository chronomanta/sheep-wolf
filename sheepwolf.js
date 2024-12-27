class SheepWolf {
    #board;
    #state;

    constructor() {}

    setBoard(board) {
        this.#board = board;
    }

    onDrop(source, target, piece, newPos, oldPos, orientation) {
        console.log("onDrop", source, target, piece, newPos, oldPos, orientation);
        if (this.#board) {
            this.#board.move('b8-a7');
        }
    }

}