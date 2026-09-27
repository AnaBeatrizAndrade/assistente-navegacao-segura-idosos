package br.ufms.assistente.navegacao.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.ufms.assistente.navegacao.model.Alerta
import br.ufms.assistente.navegacao.ui.theme.AssistenteNavegacaoTheme

@Composable
fun AlertaScreen(
    urgencia: String,
    dominio: String,
    nomeFamiliar: String,
    nomeIdoso: String,
    onSairSeguro: () -> Unit,
    onEntrarMesmoAssim: () -> Unit,
    onPedirAjuda: (nomeFamiliar: String, nomeIdoso: String) -> Unit
) {
    val isPerigoso = urgencia == Alerta.URGENCIA_ALTA
    val corAlerta = if (isPerigoso) Color(0xFFB71C1C) else Color(0xFFF57F17)

    var showConfirmacao by remember { mutableStateOf(false) }
    var ajudaEnviada by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = "Ícone de atenção",
                tint = corAlerta,
                modifier = Modifier.size(100.dp).padding(bottom = 16.dp)
            )

            Text(
                text = if (isPerigoso) "Cuidado!" else "Atenção!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = corAlerta,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .semantics { contentDescription = "Título do alerta de segurança" }
            )

            Text(
                text = if (isPerigoso)
                    "Este link pode ser uma tentativa de golpe.\n\n" +
                    "O mais seguro é sair agora."
                else
                    "Não reconhecemos este link.\n\n" +
                    "Em caso de dúvida, peça ajuda a um familiar.",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                lineHeight = 26.sp,
                color = Color(0xFF424242)
            )

            Text(
                text = "Link vereificado: $dominio",
                fontSize = 14.sp,
                color = Color(0xFF757575),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            Button(
                onClick = onSairSeguro,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .semantics { contentDescription = "Sair do link e fircar em segurança" },
                colors = ButtonDefaults.buttonColors(containerColor =  Color(0xFF2E7D32)),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Sair (Seguro)", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(16.dp))

            OutlinedButton(
                onClick = { showConfirmacao = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .semantics { contentDescription = "Entrar no link mesmo com o risco - não recomendado" },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF757575)),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Entrar mesmo assim (Arriscado)", fontSize = 15.sp)
            }

            Spacer(Modifier.height(32.dp))
            HorizontalDivider(color = Color(0xFF9E9E9E), thickness = 1.dp)
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { ajudaEnviada = true; onPedirAjuda(nomeFamiliar, nomeIdoso) },
                enabled = !ajudaEnviada,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .semantics { contentDescription = "Enviar mensagem ao familiar cadastrado pedindo ajuda" },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = if (ajudaEnviada && nomeFamiliar.isNotBlank())
                        "Abrindo mensagem para $nomeFamiliar..."
                    else "Pedir ajuda para Familiar",
                    fontSize = 16.sp
                )
            }
        }

        if (showConfirmacao) {
            ConfirmacaoRiscoDialog(
                onVoltar = { showConfirmacao = false },
                onConfirmarEntrada = { showConfirmacao = false; onEntrarMesmoAssim( )}
            )
        }
    }
}

@Composable
fun ConfirmacaoRiscoDialog(onVoltar: () -> Unit, onConfirmarEntrada: () -> Unit) {
    AlertDialog(
        onDismissRequest = onVoltar,
        title = { Text("Tem certeza?") },
        text = {
            Text("Este link pode ser perigoso. Recomendamos que você não continue\n\n.Deseja mesmo entrar?",
            fontSize = 16.sp)
        },
        confirmButton = {
            TextButton(onClick = onVoltar) { Text("Não, voltar") }
        },
        dismissButton = {
            TextButton(onClick = onConfirmarEntrada) {
                Text("Entrar mesmo assim", color = Color(0xFFB71C1C))
            }
        }
    )
}

@Preview(name = "Alerta - Perigo (vermelho)", showBackground = true)
@Composable
private fun PreviewAlertaPerigo() {
    AssistenteNavegacaoTheme {
        AlertaScreen(
            urgencia = Alerta.URGENCIA_ALTA,
            dominio = "banco-falso.com",
            nomeFamiliar = "Maria",
            nomeIdoso = "João",
            onSairSeguro = {},
            onEntrarMesmoAssim = {},
            onPedirAjuda = { _, _ ->}
        )
    }
}

@Preview(name = "Alerta - Perigo (amarelo)", showBackground = true)
@Composable
private fun PreviewAlertaSuspeito() {
    AssistenteNavegacaoTheme {
        AlertaScreen(
            urgencia = Alerta.URGENCIA_MEDIA,
            dominio = "link-desconhecido.net",
            nomeFamiliar = "Maria",
            nomeIdoso = "João",
            onSairSeguro = {},
            onEntrarMesmoAssim = {},
            onPedirAjuda = { _, _ ->}
        )
    }
}
