package br.ufms.assistente.navegacao.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.ufms.assistente.navegacao.ui.theme.AssistenteNavegacaoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamiliarCadastroScreen(
    nomeSalvo: String,
    temCadastro: Boolean,
    onSalvar: (nome: String, telefone: String) -> Unit,
    onRemover: () -> Unit,
    onVoltar: () -> Unit
) {
    var nome by remember { mutableStateOf(nomeSalvo) }
    var telefone by remember { mutableStateOf("") }
    var erroNome by remember { mutableStateOf<String?>(null) }
    var erroTel by remember { mutableStateOf<String?>(null) }
    var showConfirmacaoRemocao by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meu contato de Confiança") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Ícone de contato de confiança",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(72.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = if (temCadastro)
                    "Você pode atualizar as informações do seu contato de confiança abaixo."
                else
                    "Cadastre uma pessoa de confiança. Ela poderá ser chamada quando você receber um aviso de segurança.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it; erroNome = null },
                label = { Text("Nome da pessoa de confiança") },
                isError = erroNome != null,
                supportingText = erroNome?.let { err -> { Text(err) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .semantics { contentDescription = "Nome do familiar de confiança" },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
            )

            OutlinedTextField(
                value = telefone,
                onValueChange = { telefone = it; erroTel = null },
                label = {
                    Text(
                        if (temCadastro) "Telefone já cadastrado - deixe em branco para manter"
                        else "Telefone com DDD (ex: 67 99999-8888)"
                    )
                },
                isError = erroTel != null,
                supportingText = erroTel?.let { err -> { Text(err) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .semantics { contentDescription = "Telefone do familiar de confiança" },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
            )

            Button(
                onClick = {
                    erroNome = if (nome.isBlank()) "Por favor, digite o nome" else null
                    erroTel = if (!temCadastro && telefone.length < 10)
                        "Por favor, digite o número completo (com DDD)" else null
                    if (erroNome == null && erroTel == null)
                        onSalvar(nome.trim(), telefone.trim())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Salvar Contato", fontSize = 18.sp)
            }

            if (temCadastro) {
                Spacer(Modifier.height(16.dp))
                OutlinedButton(
                    onClick = { showConfirmacaoRemocao = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFB71C1C)),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Remover Contato", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }

    if (showConfirmacaoRemocao) {
        ConfirmacaoRemocaoDialog(
            onCancelar = { showConfirmacaoRemocao = false },
            onConfirmar = { showConfirmacaoRemocao = false; onRemover() }
        )
    }
}

@Composable
fun ConfirmacaoRemocaoDialog(onCancelar: () -> Unit, onConfirmar: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Remover contato?") },
        text = {
            Text(
                "Tem certeza que deseja remover seu contato de confiança?" +
                "Você não poderá pedir ajuda pela tela de aviso até cadastrar outro.",
                fontSize = 16.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        },
        dismissButton = {
            TextButton(onClick = onConfirmar) {
                Text("Sim, remover", color = Color(0xFFB71C1C))
            }
        }
    )
}

@Preview(name = "Familiar - Novo cadastro", showBackground = true)
@Composable
private fun PreviewFamiliarNovo() {
    AssistenteNavegacaoTheme {
        FamiliarCadastroScreen(
            nomeSalvo = "",
            temCadastro = false,
            onSalvar = { _, _ -> },
            onRemover = {},
            onVoltar = {}
        )
    }
}

@Preview(name = "Familiar - Edição de cadastro existente", showBackground = true)
@Composable
private fun PreviewFamiliarEdicao() {
    AssistenteNavegacaoTheme {
        FamiliarCadastroScreen(
            nomeSalvo = "Maria - minha filha",
            temCadastro = true,
            onSalvar = { _, _ -> },
            onRemover = {},
            onVoltar = {}
        )
    }
}
