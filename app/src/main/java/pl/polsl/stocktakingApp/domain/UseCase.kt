package pl.polsl.stocktakingApp.domain

interface UseCase<INPUT, OUTPUT> {
    operator fun invoke(input: INPUT): OUTPUT
}

interface SuspendUseCase<INPUT, OUTPUT> {
    suspend operator fun invoke(input: INPUT): OUTPUT
}