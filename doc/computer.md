Computer player `ComputerPlayerService`, with 2 strategy/implementation
1. random take from 1..3,
2. optimal strategy

For single heap, the best strategy is leaving exactly 1 match left 

```kotlin
fun getMoveAmount(heapSize: Int): Int {
    return when {
        heapSize > 3 -> 3
        heapSize > 1 -> heapSize - 1 // Leave exactly 1 to force opponent to lose
        heapSize == 1 -> 1           // Forced to take the last one and lose
        else -> 0                    // Game over
    }
}
```
