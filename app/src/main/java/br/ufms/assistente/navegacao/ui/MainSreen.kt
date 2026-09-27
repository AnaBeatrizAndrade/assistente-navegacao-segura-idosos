package br.ufms.assistente.navegacao.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.ufms.assistente.navegacao.ui.theme.AssistenteNavegacaoTheme

@Composable
fun MainSreen(
    cadastroConcluido: Boolean,
    servicoAtivo: Boolean,
    nomeUsuario: String,
    nomeFamiliarSalvo: String,
    onSalvarCadsatro: (nome: String, nomeFamiliar: String, tel: String) -> Unit,
    onAtivarProtecao: () -> Unit,
    onEditarFamiliar: () -> Unit
) {
    var showAjuda by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Cabecalho(onAjudaClick = { showAjuda = true })
        Spacer(Modifier.height(32.dp))

        if (cadastroConcluido) {
            PainelStatus(
                nomeUsuario = nomeUsuario,
                servicoAtivo = servicoAtivo,
                onAtivarProtecao = onAtivarProtecao,
                onEditarFamiliar = onEditarFamiliar
            )
        } else {
            FormularioCadastro(
                nomeFamiliarSalvo = nomeFamiliarSalvo,
                onSalvar = onSalvarCadsatro
            )
        }
    }

    if (showAjuda) DialogoAjuda(onDismiss = { showAjuda = false })
}

@Composable
fun Cabecalho(onAjudaClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Shield,
                contentDescription = "Logo do Assistente de Navegação Fundo",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp)
            )

            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Logo do Assistente de Navegação Interno",
                modifier = Modifier.size(30.dp),
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Navegação Segura",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        IconButton(
            onClick = onAjudaClick,
            modifier = Modifier.semantics { contentDescription = "Ajuda - como funciona" }
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Help,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun PainelStatus(
    nomeUsuario: String,
    servicoAtivo: Boolean,
    onAtivarProtecao: () -> Unit,
    onEditarFamiliar: () -> Unit
) {
    val primeiroNome = nomeUsuario.trim().split(" ").firstOrNull() ?: "você"
    val corStatus = if (servicoAtivo) Color(0xFF2E7D32) else Color(0xFF9E9E9E)

    Text(
        "Olá, $primeiroNome!",
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 24.dp)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    Icons.Filled.Shield,
                    contentDescription = "Ícone de status da proteção fundo",
                    tint = corStatus,
                    modifier = Modifier.size(80.dp)
                )

                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Ícone de status da proteção fundo interno",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = if (servicoAtivo) "Proteção ativa" else "Proteção desativada",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = corStatus
            )
        }
    }

    if (!servicoAtivo) {
        Button(
            onClick = onAtivarProtecao,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Ativar Proteção", fontSize = 18.sp)
        }
        Spacer(Modifier.height(16.dp))
    }

    OutlinedButton(
        onClick = onEditarFamiliar,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text("Editar Contato de Confiança", fontSize = 16.sp)
    }
}

@Composable
fun FormularioCadastro(
    nomeFamiliarSalvo: String,
    onSalvar: (nome: String, nomeFamiliar: String, tel: String) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var nomeFamiliar by remember { mutableStateOf(nomeFamiliarSalvo) }
    var tel by remember { mutableStateOf("") }
    var erroNome by remember { mutableStateOf<String?>(null) }
    var erroFamiliar by remember { mutableStateOf<String?>(null) }
    var erroTel by remember { mutableStateOf<String?>(null) }

    Text(
        "Vamos Começar!",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 24.dp)
    )
    Text(
        "Preencha as informações abaixo para ativar sua proteção.",
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 24.dp)
    )

    OutlinedTextField(
        value = nome,
        onValueChange = { nome = it; erroNome = null },
        label = { Text("Seu nome") },
        isError = erroNome != null,
        supportingText = erroNome?.let { err -> { Text(err) } },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 18.dp)
            .semantics { contentDescription = "Campo para seu nome" },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
    )

    Text(
        "Quem pode te ajudar em caso de dúvida?",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
    )

    OutlinedTextField(
        value = nomeFamiliar,
        onValueChange = { nomeFamiliar = it; erroFamiliar = null },
        label = { Text("Nome (ex: Maria - filha)") },
        isError = erroFamiliar != null,
        supportingText = erroFamiliar?.let { err -> { Text(err) } },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .semantics { contentDescription = "Nome do seu contato de confiança" },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
    )

    OutlinedTextField(
        value = tel,
        onValueChange = { tel = it; erroTel = null },
        label = { Text("Telefone com DDD (ex: 67 99999-8888)") },
        isError = erroTel != null,
        supportingText = erroTel?.let { err -> { Text(err) } },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
            .semantics { contentDescription = "Telefone do seu contato de confiança" },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
    )

    Button(
        onClick = {
            erroNome = if (nome.isBlank()) "Por favor, digite seu nome" else null
            erroFamiliar = if (nomeFamiliar.isBlank()) "Por favor, digite o nome de quem vai te ajudar" else null
            erroTel = if (tel.length < 10) "Por favor, informe o número completo (com DDD)" else null
            if (erroNome == null && erroFamiliar == null && erroTel == null)
                onSalvar(nome.trim(), nomeFamiliar.trim(), tel.trim())
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text("Salvar e Continuar", fontSize = 18.sp)
    }
}

@Composable
fun DialogoAjuda(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Como funciona?") },
        text = {
            Text(
                "Este aplicativo verifica automaticamente os links que você clica.\n\n" +
                "Se um link parecer perigoso, você verá um aviso antes de entrar no site.\n\n" +
                "Você também pode pedir ajuda para o seu familiar diretamente pelo aviso.\n\n" +
                "Nenhum dado seu é compartilhado.",
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Entendi") } }
    )
}

@Preview(name = "Tela Principal - Formulário (primeiro uso)", showBackground = true)
@Composable
private fun PreviewMainFormulario() {
    AssistenteNavegacaoTheme {
        MainSreen(
            cadastroConcluido = false,
            servicoAtivo = false,
            nomeUsuario = "",
            nomeFamiliarSalvo = "",
            onSalvarCadsatro = { _, _, _ -> },
            onAtivarProtecao = {},
            onEditarFamiliar = {}
        )
    }
}

@Preview(name = "Tela Principal - Status proteção ativa", showBackground = true)
@Composable
private fun PreviewMainStatusAtivo() {
    AssistenteNavegacaoTheme {
        MainSreen(
            cadastroConcluido = true,
            servicoAtivo = true,
            nomeUsuario = "Maria",
            nomeFamiliarSalvo = "João",
            onSalvarCadsatro = { _, _, _ -> },
            onAtivarProtecao = {},
            onEditarFamiliar = {}
        )
    }
}

@Preview(name = "Tela Principal - Status proteção inativa", showBackground = true)
@Composable
private fun PreviewMainStatusInativo() {
    AssistenteNavegacaoTheme {
        MainSreen(
            cadastroConcluido = true,
            servicoAtivo = false,
            nomeUsuario = "Maria",
            nomeFamiliarSalvo = "João",
            onSalvarCadsatro = { _, _, _ -> },
            onAtivarProtecao = {},
            onEditarFamiliar = {}
        )
    }
}
