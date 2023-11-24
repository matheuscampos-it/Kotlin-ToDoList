import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyAdapter
//Imports

class ToDoList : JFrame("To-Do List") {
    private val taskListModel = DefaultListModel<String>()
    private val completedTaskListModel = DefaultListModel<String>()
    private val taskField = JTextField(20)
    private val addButton = JButton("Add Task")
    private val deleteButton = JButton("Delete Task")
    private val renameButton = JButton("Rename Task")
    private val markAsDoneButton = JButton("Mark as Done")
    private val removeCompletedButton = JButton("Remove Completed")

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout()

        val taskJList = JList(taskListModel)
        taskJList.selectionMode = ListSelectionModel.SINGLE_SELECTION

        val completedTaskJList = JList(completedTaskListModel)
        completedTaskJList.selectionMode = ListSelectionModel.SINGLE_SELECTION

        val tabbedPane = JTabbedPane()
        tabbedPane.addTab("To-Do", JScrollPane(taskJList))
        tabbedPane.addTab("Completed", JScrollPane(completedTaskJList))
        add(tabbedPane, BorderLayout.CENTER)

        val inputPanel = JPanel()
        inputPanel.add(taskField)
        inputPanel.add(addButton)
        inputPanel.add(deleteButton)
        inputPanel.add(renameButton)
        inputPanel.add(markAsDoneButton)
        inputPanel.add(removeCompletedButton)
        add(inputPanel, BorderLayout.SOUTH)

        addButton.addActionListener(AddButtonListener())
        deleteButton.addActionListener(DeleteButtonListener(taskJList))
        renameButton.addActionListener(RenameButtonListener(taskJList))
        markAsDoneButton.addActionListener(MarkAsDoneButtonListener(taskJList, completedTaskListModel))
        removeCompletedButton.addActionListener(RemoveCompletedButtonListener(completedTaskJList))

        taskJList.addListSelectionListener {
            deleteButton.isEnabled = !taskJList.isSelectionEmpty
            renameButton.isEnabled = !taskJList.isSelectionEmpty
            markAsDoneButton.isEnabled = !taskJList.isSelectionEmpty
        }

        completedTaskJList.addListSelectionListener {
            removeCompletedButton.isEnabled = !completedTaskJList.isSelectionEmpty
        }

        // Adiciona ação ao pressionar Enter na caixa de texto
        taskField.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                if (e.keyCode == KeyEvent.VK_ENTER) {
                    addButton.doClick()
                }
            }
        })

        deleteButton.isEnabled = false
        renameButton.isEnabled = false
        markAsDoneButton.isEnabled = false
        removeCompletedButton.isEnabled = false

        pack()
        setLocationRelativeTo(null)  // Centralizar a janela
        isVisible = true
    }

    inner class AddButtonListener : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            val task = taskField.text.trim()
            if (task.isNotEmpty()) {
                taskListModel.addElement(task)
                taskField.text = ""
            }
        }
    }

    inner class DeleteButtonListener(private val taskJList: JList<String>) : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            val selectedIndex = taskJList.selectedIndex
            if (selectedIndex != -1) {
                taskListModel.removeElementAt(selectedIndex)
            }
        }
    }

    inner class RenameButtonListener(private val taskJList: JList<String>) : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            val selectedIndex = taskJList.selectedIndex
            if (selectedIndex != -1) {
                val newTaskName = JOptionPane.showInputDialog("Enter new task name:")
                if (newTaskName != null && newTaskName.trim().isNotEmpty()) {
                    taskListModel.setElementAt(newTaskName, selectedIndex)
                }
            }
        }
    }

    inner class MarkAsDoneButtonListener(
        private val taskJList: JList<String>,
        private val completedTaskListModel: DefaultListModel<String>
    ) : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            val selectedIndex = taskJList.selectedIndex
            if (selectedIndex != -1) {
                val task = taskListModel.getElementAt(selectedIndex)
                taskListModel.removeElementAt(selectedIndex)
                completedTaskListModel.addElement(task)
            }
        }
    }

    inner class RemoveCompletedButtonListener(private val completedTaskJList: JList<String>) : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            val selectedIndex = completedTaskJList.selectedIndex
            if (selectedIndex != -1) {
                completedTaskListModel.removeElementAt(selectedIndex)
            }
        }
    }
}

fun main() {
    SwingUtilities.invokeLater {
        ToDoList()
    }
}
