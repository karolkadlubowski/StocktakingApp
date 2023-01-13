package pl.polsl.stocktakingApp.screen

import app.cash.turbine.test
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import pl.polsl.stocktakingApp.base.BaseTest
import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.domain.usecase.DeleteObject
import pl.polsl.stocktakingApp.domain.usecase.GetObjectByBarcode
import pl.polsl.stocktakingApp.domain.usecase.UpsertObject
import pl.polsl.stocktakingApp.presentation.modifyObject.ModifyObjectScreenState
import pl.polsl.stocktakingApp.presentation.modifyObject.ModifyObjectScreenViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class ModifyObjectScreenViewModelTest : BaseTest<ModifyObjectScreenViewModel>() {
    private val upsertObject: UpsertObject = mockk()
    private val deleteObject: DeleteObject = mockk()
    private val getObjectByBarcode: GetObjectByBarcode = mockk()

    override val mocks: Array<Any> = arrayOf(
        upsertObject,
        deleteObject,
        getObjectByBarcode
    )

    private fun initService() {
        service = ModifyObjectScreenViewModel(
            upsertObject,
            deleteObject,
            getObjectByBarcode,
            coroutinesTestRule.testDispatcher
        )
    }

    @Test
    fun `when init invoked, state is addScreenState`() = runTestIn {
        initService()

        val updateJob = async {
            service.init(null)
        }

        val assertJob = async {
            service.state.test {
                assertInstanceOf(
                    ModifyObjectScreenState.InitialState::class.java,
                    awaitItem()
                )
                assertInstanceOf(
                    ModifyObjectScreenState.AddObjectState::class.java,
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

        awaitAll(updateJob, assertJob)
    }

    @Test
    fun `when init invoked, state is editObjectState`() = runTestIn {
        initService()

        val updateJob = async {
            service.init(StocktakingObject())
        }

        val assertJob = async {
            service.state.test {
                assertInstanceOf(
                    ModifyObjectScreenState.InitialState::class.java,
                    awaitItem()
                )
                assertInstanceOf(
                    ModifyObjectScreenState.EditObjectState::class.java,
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

        awaitAll(updateJob, assertJob)
    }

    @Test
    fun `when deleteObject invoked, invoke deleteObject use case`() = runTestIn {
        coEvery {
            deleteObject(any())
        } just runs

        initService()

        service.init(StocktakingObject())

        service.deleteObject()

        coVerify(exactly = 1) { deleteObject(any()) }
    }

    @Test
    fun `when object does not exist and state is addObjectState, emit ObjectUpsertSuccess event`() =
        runTestIn {
            coEvery { getObjectByBarcode(any()) } returns null
            coEvery { upsertObject(any()) } just runs

            initService()

            val updateJob = async {
                service.init(null)
                service.state.test {
                    assertInstanceOf(
                        ModifyObjectScreenState.InitialState::class.java,
                        awaitItem()
                    )
                    assertInstanceOf(
                        ModifyObjectScreenState.AddObjectState::class.java,
                        awaitItem()
                    )
                }
                service.setAmount("11")
                service.setBarcode("barcode")
                service.setDescription("desc")
                service.setName("name")
                delay(1000)
                service.upsertObject()
            }

            val assertEventJob = async {
                service.events.test {
                    assertInstanceOf(
                        ModifyObjectScreenViewModel.ObjectUpsertSuccess::class.java,
                        awaitItem()
                    )
                    cancelAndIgnoreRemainingEvents()
                }
            }

            awaitAll(updateJob, assertEventJob)

            coVerify(exactly = 1) {
                getObjectByBarcode(any())
            }
            coVerify(exactly = 1) {
                upsertObject(any())
            }
        }

    @Test
    fun `when object exists and state is addObjectState, emit ObjectAlreadyExists event`() =
        runTestIn {
            coEvery { getObjectByBarcode(any()) } returns StocktakingObject()

            initService()

            val updateJob = async {
                service.init(null)
                service.state.test {
                    assertInstanceOf(
                        ModifyObjectScreenState.InitialState::class.java,
                        awaitItem()
                    )
                    assertInstanceOf(
                        ModifyObjectScreenState.AddObjectState::class.java,
                        awaitItem()
                    )
                }
                service.setAmount("11")
                service.setBarcode("barcode")
                service.setDescription("desc")
                service.setName("name")
                delay(1000)
                service.upsertObject()
            }

            val assertEventJob = async {
                service.events.test {
                    assertInstanceOf(
                        ModifyObjectScreenViewModel.ObjectAlreadyExists::class.java,
                        awaitItem()
                    )
                    cancelAndIgnoreRemainingEvents()
                }
            }

            awaitAll(updateJob, assertEventJob)

            coVerify(exactly = 1) {
                getObjectByBarcode(any())
            }
        }

    @Test
    fun `when fields are empty, emit emptyTextFields event`() =
        runTestIn {
            coEvery { getObjectByBarcode(any()) } returns null

            initService()

            val updateJob = async {
                service.init(null)
                service.state.test {
                    assertInstanceOf(
                        ModifyObjectScreenState.InitialState::class.java,
                        awaitItem()
                    )
                    assertInstanceOf(
                        ModifyObjectScreenState.AddObjectState::class.java,
                        awaitItem()
                    )
                }
                delay(1000)
                service.upsertObject()
            }

            val assertEventJob = async {
                service.events.test {
                    assertInstanceOf(
                        ModifyObjectScreenViewModel.EmptyTextFields::class.java,
                        awaitItem()
                    )
                    cancelAndIgnoreRemainingEvents()
                }
            }

            awaitAll(updateJob, assertEventJob)

            coVerify(exactly = 1) {
                getObjectByBarcode(any())
            }
        }
}