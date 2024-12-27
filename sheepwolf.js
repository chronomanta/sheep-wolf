class SheepWolf {
    #sheeps = [];
    #wolf = '';
    #turn = '';

    #onWhiteMoveCallback;
    #onBlackMoveCallback;

    constructor(onWhiteMoveCallback, onBlackMoveCallback) {
        this.#onWhiteMoveCallback = onWhiteMoveCallback;
        this.#onBlackMoveCallback = onBlackMoveCallback;
    }

    gameOver() {
        return false; // TODO
    }

    turn() {
        return this.#turn;
    }

    move(from, to) {
        if (this.#turn === 'w' && !this.#sheeps.includes(from)) {
            return null;
        } else if (this.#turn === 'b' && this.#wolf !== from) {
            return null;
        } else if (!this.moves(from).includes(to)) {
            return null;
        }
        if (this.#turn === 'w') {
            this.#sheeps[this.#sheeps.indexOf(from)] = to;
            this.#turn = 'b';
        } else if (this.#turn === 'b') {
            this.#wolf = to;
            this.#turn = 'w';
        }
        return true;
    }

    moves(from) {
        if (this.#turn === 'w') {
            if (!this.#sheeps.includes(from)) {
                return [];
            }
        } else {
            if (this.#wolf !== from) {
                return [];
            }
        }
        return this.#possibleMoves(this.#turn, from);
    }

    startGame() {
        this.#sheeps = ['a1', 'c1', 'e1', 'g1'];
        this.#wolf = 'd8';
        this.#turn = 'w';
        return '3p4/8/8/8/8/8/8/P1P1P1P1';
    }

    #neighbourLetters(letter) {
        switch (letter) {
            case 'a': return ['b'];
            case 'b': return ['a', 'c'];
            case 'c': return ['b', 'd'];
            case 'd': return ['c', 'e'];
            case 'e': return ['d', 'f'];
            case 'f': return ['e', 'g'];
            case 'g': return ['f', 'h'];
            case 'h': return ['g'];
            default: throw "Wrong position: [" + letter + "]";
        }
    }

    #possibleMoves(side, from) {
        let fromLetter = this.#posLetter(from);
        let fromNumber = this.#posNumber(from);
        let ret = [];
        if (side === 'w') {
            if (fromNumber === 8) return [];
            let toNumber = fromNumber + 1;
            ret = this.#neighbourLetters(fromLetter).map((letter) => letter + toNumber);
        } else {
            let toNumbers = [];
            switch (fromNumber) {
                case 1: 
                    toNumbers = [2];
                    break;
                case 8:
                    toNumbers = [7];
                    break;
                default:
                    toNumbers = [fromNumber-1, fromNumber+1];
            }
            ret = this.#neighbourLetters(fromLetter).flatMap((letter) => toNumbers.map((num) => letter+num));
        }
        return ret.filter(pos => pos !== this.#wolf && !this.#sheeps.includes(pos));
    }

    #posLetter(pos) {
        return "" + pos.charAt(0);
    }

    #posNumber(pos) {
        return Number(pos.charAt(1));
    }

    #wolfWon() {
        let wolfRange = this.#posNumber(this.#wolf);
        let minSheepRange = Math.min.apply(Math, this.#sheeps.map(this.#posNumber));
        return wolfRange <= minSheepRange;
    }

    #sheepsWon() {
        return this.#possibleMoves('b', this.#wolf).length === 0;
    }

}