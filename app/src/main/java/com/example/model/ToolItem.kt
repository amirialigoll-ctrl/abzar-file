package com.example.model

data class ToolItem(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String
)

val availableTools = listOf(
    ToolItem("converter", "Universal Converter", "Convert between PDF, Word, Excel, Images", "Sync"),
    ToolItem("compressor", "File Compressor", "Reduce file size quickly", "Compress"),
    ToolItem("viewer", "Viewer & Editor", "Open and edit files inline", "Edit"),
    ToolItem("extract_txt", "Extract Text", "Pull clean text from documents", "TextSnippet"),
    ToolItem("merge", "Merge Files", "Combine multiple files into one", "Merge"),
    ToolItem("split", "Split Files", "Divide files into separate outputs", "CallSplit"),
    ToolItem("scanner", "Doc Scanner", "Scan paper to PDF directly", "DocumentScanner"),
    ToolItem("esign", "eSign PDF", "Draw and place your signature", "Draw"),
    ToolItem("toolbox", "Page Toolbox", "Rearrange & delete pages", "Pages"),
    ToolItem("annotate", "Annotate", "Add notes and drawings", "Brush"),
    ToolItem("secure", "Secure Files", "Add or remove password", "Lock"),
    ToolItem("ai_assistant", "AI Assistant", "Smart assistant for your files", "AutoAwesome")
)
