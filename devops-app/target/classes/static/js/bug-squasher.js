(function() {
    const bugCanvas = document.getElementById('gameCanvas');
    const bugCtx = bugCanvas.getContext('2d');
    const bugScoreEl = document.getElementById('score');

    let bugs = [];
    let bugGameLoop;
    let bugScore = 0;
    let bugIsRunning = false;

    class Bug {
        constructor() {
            this.size = 30;
            this.x = Math.random() * (bugCanvas.width - this.size);
            this.y = Math.random() * (bugCanvas.height - this.size);
            this.speedX = (Math.random() - 0.5) * 4;
            this.speedY = (Math.random() - 0.5) * 4;
            this.life = 100;
        }

        draw() {
            bugCtx.fillStyle = '#ef4444';
            bugCtx.shadowBlur = 15;
            bugCtx.shadowColor = '#ef4444';
            bugCtx.beginPath();
            bugCtx.arc(this.x + this.size/2, this.y + this.size/2, this.size/2, 0, Math.PI * 2);
            bugCtx.fill();
            bugCtx.shadowBlur = 0;
        }

        update() {
            this.x += this.speedX;
            this.y += this.speedY;

            if (this.x < 0 || this.x > bugCanvas.width - this.size) this.speedX *= -1;
            if (this.y < 0 || this.y > bugCanvas.height - this.size) this.speedY *= -1;
        }
    }

    function updateBugGame() {
        bugCtx.fillStyle = '#000';
        bugCtx.fillRect(0, 0, bugCanvas.width, bugCanvas.height);

        if (Math.random() < 0.03 && bugs.length < 10) {
            bugs.push(new Bug());
        }

        bugs.forEach((bug, index) => {
            bug.update();
            bug.draw();
        });
    }

    function handleBugClick(e) {
        if (!bugIsRunning) return;
        const rect = bugCanvas.getBoundingClientRect();
        const mouseX = e.clientX - rect.left;
        const mouseY = e.clientY - rect.top;

        bugs.forEach((bug, index) => {
            const dist = Math.sqrt((mouseX - (bug.x + bug.size/2))**2 + (mouseY - (bug.y + bug.size/2))**2);
            if (dist < bug.size/2 + 10) {
                bugs.splice(index, 1);
                bugScore += 50;
                bugScoreEl.innerText = bugScore;
                if (typeof addLog === 'function') addLog('Production bug squashed! Patch deployed.', 'success');
            }
        });
    }

    window.startBugGame = function() {
        if (bugIsRunning) return;
        if (typeof window.stopGame === 'function') window.stopGame();
        bugIsRunning = true;
        bugs = [];
        bugScore = 0;
        bugScoreEl.innerText = bugScore;
        bugGameLoop = setInterval(updateBugGame, 30);
        bugCanvas.addEventListener('mousedown', handleBugClick);
    };

    window.stopBugGame = function() {
        clearInterval(bugGameLoop);
        bugIsRunning = false;
        bugCanvas.removeEventListener('mousedown', handleBugClick);
    };
})();
