(function() {
    const canvas = document.getElementById('gameCanvas');
    const ctx = canvas.getContext('2d');
    const scoreElement = document.getElementById('score');
    const startBtn = document.getElementById('startBtn');

    const gridSize = 20;
    const tileCount = canvas.width / gridSize;

    let score = 0;
    let dx = 0;
    let dy = 0;
    let snake = [
        {x: 10, y: 10},
        {x: 10, y: 11},
        {x: 10, y: 12}
    ];
    let food = {x: 5, y: 5};
    let gameLoop;
    let isRunning = false;

    function drawGame() {
        clearCanvas();
        moveSnake();
        checkCollision();
        drawFood();
        drawSnake();
    }

    function clearCanvas() {
        ctx.fillStyle = '#000';
        ctx.fillRect(0, 0, canvas.width, canvas.height);
    }

    function drawSnake() {
        snake.forEach((part, index) => {
            const isHead = index === 0;
            ctx.fillStyle = isHead ? '#00f2fe' : '#4facfe';
            
            if (isHead) {
                ctx.shadowBlur = 15;
                ctx.shadowColor = '#00f2fe';
            } else {
                ctx.shadowBlur = 0;
            }

            ctx.fillRect(part.x * gridSize, part.y * gridSize, gridSize - 2, gridSize - 2);
        });
        ctx.shadowBlur = 0;
    }

    function moveSnake() {
        const head = {x: snake[0].x + dx, y: snake[0].y + dy};
        snake.unshift(head);

        if (head.x === food.x && head.y === food.y) {
            score += 10;
            scoreElement.innerText = score;
            spawnFood();
        } else {
            snake.pop();
        }
    }

    function spawnFood() {
        food = {
            x: Math.floor(Math.random() * tileCount),
            y: Math.floor(Math.random() * tileCount)
        };
    }

    function drawFood() {
        ctx.fillStyle = '#f093fb';
        ctx.shadowBlur = 10;
        ctx.shadowColor = '#f093fb';
        ctx.beginPath();
        ctx.arc(food.x * gridSize + gridSize/2, food.y * gridSize + gridSize/2, gridSize/3, 0, Math.PI * 2);
        ctx.fill();
        ctx.shadowBlur = 0;
    }

    function checkCollision() {
        const head = snake[0];
        if (head.x < 0 || head.x >= tileCount || head.y < 0 || head.y >= tileCount) {
            gameOver();
        }
        for (let i = 1; i < snake.length; i++) {
            if (head.x === snake[i].x && head.y === snake[i].y) {
                gameOver();
            }
        }
    }

    function gameOver() {
        clearInterval(gameLoop);
        isRunning = false;
        if (typeof window.showToast === 'function') {
            window.showToast(`Game Over! Score: ${score}`, 'error');
        }
        resetGame();
    }

    function resetGame() {
        score = 0;
        scoreElement.innerText = score;
        snake = [{x: 10, y: 10}, {x: 10, y: 11}, {x: 10, y: 12}];
        dx = 0; dy = -1;
        startBtn.innerText = 'Start Game';
    }

    window.startGame = function() {
        if (isRunning) return;
        if (typeof window.stopBugGame === 'function') window.stopBugGame();
        isRunning = true;
        dx = 0; dy = -1;
        startBtn.innerText = 'Restart';
        gameLoop = setInterval(drawGame, 100);
    };

    window.stopGame = function() {
        clearInterval(gameLoop);
        isRunning = false;
    };

    document.addEventListener('keydown', (e) => {
        if (!isRunning) return;
        switch (e.key) {
            case 'ArrowUp': case 'w': if (dy !== 1) { dx = 0; dy = -1; } break;
            case 'ArrowDown': case 's': if (dy !== -1) { dx = 0; dy = 1; } break;
            case 'ArrowLeft': case 'a': if (dx !== 1) { dx = -1; dy = 0; } break;
            case 'ArrowRight': case 'd': if (dx !== -1) { dx = 1; dy = 0; } break;
        }
    });

    spawnFood();
    clearCanvas();
    drawSnake();
    drawFood();
})();
