package com.yanbin.reactivestickynote.editor.domain

import com.yanbin.reactivestickynote.account.AccountService
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.model.*
import com.yanbin.utils.fold
import com.yanbin.utils.toOptional
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.util.*

class Editor(
    private val noteRepository: NoteRepository,
    private val accountService: AccountService
) {

    private val _showContextMenu = MutableStateFlow(false)
    private val _showAddButton = MutableStateFlow(true)
    private val openEditTextScreenSignal = MutableSharedFlow<StickyNote>()
    private val editorScope = CoroutineScope(Dispatchers.Default)

    val selectedNotes: SharedFlow<List<SelectedNote>> = noteRepository.getAllSelectedNotes().shareIn(editorScope, SharingStarted.Lazily, replay = 1)
    val isContextMenuShown: StateFlow<Boolean> = _showContextMenu.asStateFlow()
    val isAddButtonShown: StateFlow<Boolean> = _showAddButton.asStateFlow()

    val userSelectedNote: SharedFlow<Optional<SelectedNote>> = selectedNotes.map { notes ->
        Optional.ofNullable(notes.find { note ->
            note.userName == runBlocking {
                accountService.getCurrentAccount().userName
            }
        })
    }.onStart { emit(Optional.empty()) }
        .shareIn(editorScope, SharingStarted.Lazily, replay = 1)

    // State
    val selectedStickyNote: SharedFlow<Optional<StickyNote>> = userSelectedNote
        .flatMapLatest { optSelectedNote ->
            optSelectedNote.fold(
                someFun = { note ->
                    getNoteById(note.noteId)
                        .map { it.toOptional() }
                },
                emptyFun = {
                    flowOf(Optional.empty())
                }
            )
        }
        .shareIn(editorScope, SharingStarted.Lazily, replay = 1)

    val openEditTextScreen: SharedFlow<StickyNote> = openEditTextScreenSignal.asSharedFlow()

    // Component
    val contextMenu = ContextMenu(selectedStickyNote)
    val viewPort = ViewPort(noteRepository)

    suspend fun setNoteSelected(id: String) {
        noteRepository.setNoteSelection(id, accountService.getCurrentAccount())
    }

    suspend fun setNoteUnSelected(id: String) {
        noteRepository.removeNoteSelection(id, accountService.getCurrentAccount())
    }

    suspend fun showAddButton() {
        _showAddButton.emit(true)
        _showContextMenu.emit(false)
    }

    suspend fun showContextMenu() {
        _showAddButton.emit(false)
        _showContextMenu.emit(true)
    }

    fun getNoteById(id: String): Flow<StickyNote> {
        return noteRepository.getNoteById(id)
    }

    suspend fun navigateToEditTextPage(stickyNote: StickyNote) {
        openEditTextScreenSignal.emit(stickyNote)
    }

    fun onDestroy() {
        editorScope.cancel()
    }

}
