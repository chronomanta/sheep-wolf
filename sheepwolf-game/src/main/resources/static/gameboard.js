class Gameboard {

    #boardSelector;
    #stompClient;

    #whiteSquareGrey = '#a9a9a9';
    #blackSquareGrey = '#696969';
    #redSquare = '#992211';
    #greenSquare = '#11aa22';
    #game;
    #board;

    #gameStarted = false;
    #gameOver = false;
    #side = ' ';

    constructor(boardSelector, stompClient) {
        this.#boardSelector = boardSelector;
        this.#stompClient = stompClient;
        this.#game = new SheepWolf();
    }

    init() {
        let gameboard = this;
        this.#stompClient.subscribe('/user/queue/game-started', (gameStarted) => {
            console.log('Received game started: ' + gameStarted.body);
            gameboard.#onGameStart(JSON.parse(gameStarted.body).side);
        });
    }

    #onDragStart (source, piece) {
        let game = this.#game;
        if (game.gameOver() || game.turn() !== this.#side) return false

        if ((game.turn() === 'w' && piece.search(/^b/) !== -1) ||
            (game.turn() === 'b' && piece.search(/^w/) !== -1)) {
            return false
        }
    }

    #onDrop (source, target) {
        this.#clearBackgrounds();
        let game = this.#game;

        let move = game.move(source, target);

        if (move === null) return 'snapback';

        this.#stompClient.publish({
            destination: '/game/move',
            body: JSON.stringify({
                from: source,
                to: target
            })
        });
    }

    #clearBackgrounds () {
        if (!this.#gameOver) {
            $('#' + this.#boardSelector + ' .square-55d63').css('background', '');
        }
    }

    #setBackground (square, whiteSquare, blackSquare) {
        let $square = $('#' + this.#boardSelector + ' .square-' + square);

        let background = whiteSquare;
        if (blackSquare && $square.hasClass('black-3c85d')) {
            background = blackSquare;
        }

        $square.css('background', background);
    }

    #onMouseoverSquare (square, piece) {
        let game = this.#game;
        let moves = game.moves(square)

        if (moves.length === 0 || game.turn() !== this.#side) return;

        this.#setBackground(square, this.#whiteSquareGrey, this.#blackSquareGrey);

        for (let i = 0; i < moves.length; i++) {
            this.#setBackground(moves[i], this.#whiteSquareGrey, this.#blackSquareGrey);
        }
    }

    #onMouseoutSquare (square, piece) {
        this.#clearBackgrounds();
    }

    #applyMove(move) {
        let game = this.#game;
        let board = this.#board;

        game.move(move.from, move.to);
        board.move(move.from + '-' + move.to);
    }

    #onGameStart(side) {
        let game = this.#game;
        let gameboard = this;

        this.#board = Chessboard(this.#boardSelector, {
            draggable: true,
            position: game.startGame(),
            orientation: side === 'SHEEP' ? 'white' : side === 'WOLF' ? 'black' : 'white',
            onDragStart: gameboard.#onDragStart.bind(gameboard),
            onDrop: gameboard.#onDrop.bind(gameboard),
            onMouseoutSquare: gameboard.#onMouseoutSquare.bind(gameboard),
            onMouseoverSquare: gameboard.#onMouseoverSquare.bind(gameboard)
        });

        this.#side = side === 'SHEEP' ? 'w' : side === 'WOLF' ? 'b' : ' ';
        this.#gameStarted = true;

        this.#stompClient.subscribe('/user/queue/move', (move) => {
            console.log('Received move: ' + move.body);
            this.#applyMove(JSON.parse(move.body));
        });

        this.#stompClient.subscribe('/user/queue/game-over', (gameOver) => {
            console.log('Received game over: ' + gameOver.body);
            this.#onGameOver(JSON.parse(gameOver.body).winner);
        });
    }

    #onGameOver(winner) {
        this.#gameOver = true;
        let winningColor = winner === 'SHEEP' ? 'w' : winner === 'WOLF' ? 'b' : ' ';
        let game = this.#game;

        if (this.#gameStarted) {
            if (winner === 'SHEEP') {
                this.#setBackground(game.getWolf(), this.#redSquare);
                game.getSheeps().forEach((sheep) => this.#setBackground(sheep, this.#greenSquare));
            } else if (winner === 'WOLF') {
                this.#setBackground(game.getWolf(), this.#greenSquare);
                game.getSheeps().forEach((sheep) => this.#setBackground(sheep, this.#redSquare));
            }
            this.#gameStarted = false;
        }

        if (winningColor === this.#side) {
            setTimeout(() => alert('You won!'), 100);
        } else {
            setTimeout(() => alert('You lost!'), 100);
        }

    }

}