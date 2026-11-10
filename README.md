# LUAX ESP Overlay v3

Projeto Android/Kotlin para Android Studio e build debug via GitHub Actions.

## Interface
- Painel escuro compacto com acento verde, estilo mod-menu.
- Toggles para Box em cantos, Skeleton, Distância e Box preenchida (desligada por padrão).
- Ajustes de espessura e limite de distância.
- Box, skeleton e distância totalmente brancas; preenchimento opcional branco e translúcido, desligado por padrão.
- Box segmentada nos cantos; skeleton e distância são desenhadas dentro/abaixo da box.

## Dados e VPN
Este projeto usa um alvo fictício para demonstrar a interface. Uma VPN local não consegue inferir coordenadas internas de qualquer jogo: ela transporta pacotes, mas não transforma tráfego criptografado em posições de jogadores. Para dados reais, integre um endpoint/telemetria no seu próprio jogo ou em um servidor que você controla e envie posição 3D, câmera e juntas do esqueleto em um formato documentado. Não há captura de pacotes, leitura de memória ou bypass de anti-cheat.

## Build
Abra o projeto no Android Studio ou envie ao GitHub. O workflow `Android Debug APK` gera `app-debug.apk` como artifact.
