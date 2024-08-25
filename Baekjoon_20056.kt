import java.util.*

private var n = 0
private var m = 0
private var k = 0
private lateinit var map : Array<Array<Queue<FireBall>>>
private val afterFireBallInfos: Queue<FireBall> = LinkedList()
private val moveDir = arrayOf(-1 to 0, -1 to 1, 0 to 1, 1 to 1, 1 to 0, 1 to -1, 0 to -1, -1 to -1)

data class FireBall(
    val row: Int = 0,
    val col: Int = 0,
    val mass: Int = 0,
    val speed: Int = 0,
    val dir: Int = 0
)

fun main() {
    input()
    progress()
}

private fun input() {
    val nmk = readLine()!!.split(" ").map { it.toInt() }
    n = nmk[0]; m = nmk[1]; k = nmk[2]

    map = Array(n + 1) { Array(n + 1) { LinkedList() } }

    repeat(m) {
        val (r, c, m, s, d) = readLine()!!.split(" ").map { it.toInt() }
        val fireBall = FireBall(r, c, m, s, d)
        map[r][c].add(fireBall)
    }
}

private fun progress() {
    repeat(k) {
        saveMoveInfo()
        move()
        addUp()
    }

    println(massSum())
}

private fun saveMoveInfo() {
    for(i in 1 .. n) {
        for(j in 1 .. n) {
            if(map[i][j].size == 0) continue

            while(map[i][j].isNotEmpty()) {
                val curFireballInfo = map[i][j].poll()

                val curY = curFireballInfo.row
                val curX = curFireballInfo.col
                val curMass = curFireballInfo.mass
                val curSpeed = curFireballInfo.speed
                val curDir = curFireballInfo.dir

                val tempY = curY + (moveDir[curDir].first * curSpeed) % n
                val nextY = if(tempY < 1) tempY + n else if(tempY > n) tempY - n else tempY

                val tempX = curX + (moveDir[curDir].second * curSpeed) % n
                val nextX = if(tempX < 1) tempX + n else if(tempX > n) tempX - n else tempX

                val nextFireBallInfo = FireBall(nextY, nextX, curMass, curSpeed, curDir)
                afterFireBallInfos.add(nextFireBallInfo)
            }
        }
    }
}

private fun move() {
    while(afterFireBallInfos.isNotEmpty()) {
        val fireBallInfo = afterFireBallInfos.poll()
        val y = fireBallInfo.row
        val x = fireBallInfo.col

        map[y][x].add(fireBallInfo)
    }
}

private fun addUp() {
    for(y in 1 .. n) {
        for(x in 1 .. n) {
            if(map[y][x].size < 2) continue

            val cnt = map[y][x].size

            var totalMass = 0
            var totalSpeed = 0
            val dirList = mutableListOf<Int>()

            while(map[y][x].isNotEmpty()) {
                val fireBallInfo = map[y][x].poll()
                totalMass += fireBallInfo.mass
                totalSpeed += fireBallInfo.speed
                dirList.add(fireBallInfo.dir % 2)
            }

            val divideMass = totalMass / 5
            val divideSpeed = totalSpeed / cnt
            val divideDir = dirList.checkDir()

            if(divideMass == 0) continue

            divideDir.forEach {
                val divideFireBallInfo = FireBall(y, x, divideMass, divideSpeed, it)
                map[y][x].add(divideFireBallInfo)
            }
        }
    }
}

private fun List<Int>.checkDir(): IntArray {
    val cnt = this.size

    if(this.count { it == 0 } == cnt || this.count { it == 1 } == cnt) return intArrayOf(0, 2, 4, 6)
    return intArrayOf(1, 3, 5, 7)
}

private fun massSum(): Int {
    var totalMass = 0

    for(y in 1 .. n) {
        for(x in 1 .. n) {
            if(map[y][x].size == 0) continue

            while(map[y][x].isNotEmpty()) {
                val curFireBallInfo = map[y][x].poll()
                totalMass += curFireBallInfo.mass
            }
        }
    }

    return totalMass
}